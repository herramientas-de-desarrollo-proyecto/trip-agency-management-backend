package com.tripagencymanagement.template.auth.presentation.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripagencymanagement.template.auth.application.commands.ChangePasswordCommand;
import com.tripagencymanagement.template.auth.application.commands.LoginCommand;
import com.tripagencymanagement.template.auth.application.commands.LogoutCommand;
import com.tripagencymanagement.template.auth.application.commands.RefreshTokenCommand;
import com.tripagencymanagement.template.auth.application.commands.RevokeAllSessionsCommand;
import com.tripagencymanagement.template.auth.application.commands.handlers.ChangePasswordCommandHandler;
import com.tripagencymanagement.template.auth.application.commands.handlers.LoginCommandHandler;
import com.tripagencymanagement.template.auth.application.commands.handlers.LogoutCommandHandler;
import com.tripagencymanagement.template.auth.application.commands.handlers.RefreshTokenCommandHandler;
import com.tripagencymanagement.template.auth.application.commands.handlers.RevokeAllSessionsCommandHandler;
import com.tripagencymanagement.template.auth.application.queries.GetCurrentUserQuery;
import com.tripagencymanagement.template.auth.application.queries.GetUserSessionsQuery;
import com.tripagencymanagement.template.auth.application.queries.handlers.GetCurrentUserQueryHandler;
import com.tripagencymanagement.template.auth.application.queries.handlers.GetUserSessionsQueryHandler;
import com.tripagencymanagement.template.auth.domain.entities.DAuthenticatedUser;
import com.tripagencymanagement.template.auth.presentation.dto.AuthResponseDto;
import com.tripagencymanagement.template.auth.presentation.dto.ChangePasswordRequestDto;
import com.tripagencymanagement.template.auth.presentation.dto.ChangePasswordResponseDto;
import com.tripagencymanagement.template.auth.presentation.dto.LoginRequestDto;
import com.tripagencymanagement.template.auth.presentation.dto.LogoutResponseDto;
import com.tripagencymanagement.template.auth.presentation.dto.RefreshTokenRequestDto;
import com.tripagencymanagement.template.auth.presentation.dto.SessionInfoDto;
import com.tripagencymanagement.template.general.presentation.controllers.BaseV1Controller;
import com.tripagencymanagement.template.general.presentation.exception.ErrorBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST Controller for authentication operations.
 * Handles login, logout, token refresh, and session management.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints para autenticación y gestión de sesiones")
@RequiredArgsConstructor
public class AuthController extends BaseV1Controller {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final LoginCommandHandler loginCommandHandler;
    private final LogoutCommandHandler logoutCommandHandler;
    private final RefreshTokenCommandHandler refreshTokenCommandHandler;
    private final RevokeAllSessionsCommandHandler revokeAllSessionsCommandHandler;
    private final ChangePasswordCommandHandler changePasswordCommandHandler;
    private final GetCurrentUserQueryHandler getCurrentUserQueryHandler;
    private final GetUserSessionsQueryHandler getUserSessionsQueryHandler;

    @PostMapping("/login")
    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica un usuario y devuelve los tokens JWT de acceso y refresh.",
        responses = {
            @ApiResponse(
                responseCode = "200", 
                description = "Autenticación exitosa",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "401", 
                description = "Credenciales inválidas",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorBody.class))
            ),
            @ApiResponse(
                responseCode = "400", 
                description = "Datos de solicitud inválidos",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody LoginRequestDto loginRequest,
            HttpServletRequest request
    ) {
        log.info("🔐 [LOGIN] Request received");
        log.info("🔐 [LOGIN] Content-Type: {}", request.getContentType());
        log.info("🔐 [LOGIN] Email: {}", loginRequest.getEmail());
        
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = getClientIpAddress(request);
        
        log.info("🔐 [LOGIN] User-Agent: {}", userAgent);
        log.info("🔐 [LOGIN] IP: {}", ipAddress);

        LoginCommand command = new LoginCommand(loginRequest, userAgent, ipAddress);
        AuthResponseDto response = loginCommandHandler.execute(command);
        
        log.info("🔐 [LOGIN] Success for user: {}", loginRequest.getEmail());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(
        summary = "Cerrar sesión",
        description = "Cierra la sesión actual del usuario revocando el token de acceso.",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(
                responseCode = "200", 
                description = "Sesión cerrada exitosamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = LogoutResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "401", 
                description = "No autorizado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    public ResponseEntity<LogoutResponseDto> logout(
            @AuthenticationPrincipal DAuthenticatedUser authenticatedUser,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        // Handle case when user is not authenticated or token is invalid
        if (authenticatedUser == null) {
            log.warn("🔐 [LOGOUT] Attempted logout without valid authentication");
            // Return success anyway - the user wants to logout
            return ResponseEntity.ok(LogoutResponseDto.builder().success(true).message("Sesión cerrada").build());
        }
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("🔐 [LOGOUT] No valid Authorization header");
            return ResponseEntity.ok(LogoutResponseDto.builder().success(true).message("Sesión cerrada").build());
        }
        
        // Remove "Bearer " prefix and clean whitespace
        String token = authHeader.substring(7).trim().replaceAll("\\s+", "");
        
        log.info("🔐 [LOGOUT] Processing logout for user ID: {}", authenticatedUser.getUserId());
        
        LogoutCommand command = new LogoutCommand(token, authenticatedUser.getUserId());
        LogoutResponseDto response = logoutCommandHandler.execute(command);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(
        summary = "Refrescar tokens",
        description = "Genera nuevos tokens de acceso y refresh usando un refresh token válido.",
        responses = {
            @ApiResponse(
                responseCode = "200", 
                description = "Tokens refrescados exitosamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "401", 
                description = "Refresh token inválido o expirado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    public ResponseEntity<AuthResponseDto> refreshToken(
            @Valid @RequestBody RefreshTokenRequestDto refreshRequest,
            HttpServletRequest request
    ) {
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = getClientIpAddress(request);

        RefreshTokenCommand command = new RefreshTokenCommand(
                refreshRequest.getRefreshToken(), 
                userAgent, 
                ipAddress
        );
        AuthResponseDto response = refreshTokenCommandHandler.execute(command);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout-all")
    @Operation(
        summary = "Cerrar todas las sesiones",
        description = "Cierra todas las sesiones activas del usuario en todos los dispositivos.",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(
                responseCode = "200", 
                description = "Todas las sesiones cerradas exitosamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = LogoutResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "401", 
                description = "No autorizado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    public ResponseEntity<LogoutResponseDto> logoutAll(
            @AuthenticationPrincipal DAuthenticatedUser authenticatedUser
    ) {
        // Handle case when user is not authenticated
        if (authenticatedUser == null) {
            log.warn("🔐 [LOGOUT-ALL] Attempted logout-all without valid authentication");
            return ResponseEntity.ok(LogoutResponseDto.builder().success(true).message("Todas las sesiones cerradas").build());
        }
        
        log.info("🔐 [LOGOUT-ALL] Processing logout-all for user ID: {}", authenticatedUser.getUserId());
        
        RevokeAllSessionsCommand command = new RevokeAllSessionsCommand(authenticatedUser.getUserId());
        LogoutResponseDto response = revokeAllSessionsCommandHandler.execute(command);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    @Operation(
        summary = "Cambiar contraseña",
        description = "Cambia la contraseña del usuario autenticado.",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(
                responseCode = "200", 
                description = "Contraseña cambiada exitosamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChangePasswordResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "400", 
                description = "Datos de solicitud inválidos o contraseña no coincide",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorBody.class))
            ),
            @ApiResponse(
                responseCode = "401", 
                description = "Contraseña actual incorrecta o no autorizado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    public ResponseEntity<ChangePasswordResponseDto> changePassword(
            @AuthenticationPrincipal DAuthenticatedUser authenticatedUser,
            @Valid @RequestBody ChangePasswordRequestDto changePasswordRequest
    ) {
        log.info("🔑 [CHANGE_PASSWORD] Request received for user ID: {}", authenticatedUser.getUserId());
        
        ChangePasswordCommand command = new ChangePasswordCommand(
                authenticatedUser.getUserId(), 
                changePasswordRequest
        );
        ChangePasswordResponseDto response = changePasswordCommandHandler.execute(command);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Operation(
        summary = "Obtener usuario actual",
        description = "Obtiene la información del usuario autenticado actualmente.",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(
                responseCode = "200", 
                description = "Información del usuario obtenida exitosamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDto.UserInfoDto.class))
            ),
            @ApiResponse(
                responseCode = "401", 
                description = "No autorizado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    public ResponseEntity<AuthResponseDto.UserInfoDto> getCurrentUser(
            @AuthenticationPrincipal DAuthenticatedUser authenticatedUser
    ) {
        GetCurrentUserQuery query = new GetCurrentUserQuery(authenticatedUser.getUserId());
        AuthResponseDto.UserInfoDto response = getCurrentUserQueryHandler.execute(query);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/sessions")
    @Operation(
        summary = "Obtener sesiones activas",
        description = "Obtiene la lista de sesiones activas del usuario en todos los dispositivos.",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(
                responseCode = "200", 
                description = "Lista de sesiones obtenida exitosamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = SessionInfoDto.class))
            ),
            @ApiResponse(
                responseCode = "401", 
                description = "No autorizado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    public ResponseEntity<List<SessionInfoDto>> getUserSessions(
            @AuthenticationPrincipal DAuthenticatedUser authenticatedUser,
            @RequestHeader("Authorization") String authHeader
    ) {
        String currentToken = authHeader.substring(7);
        GetUserSessionsQuery query = new GetUserSessionsQuery(authenticatedUser.getUserId());
        List<SessionInfoDto> sessions = getUserSessionsQueryHandler.execute(query, currentToken);

        return ResponseEntity.ok(sessions);
    }

    /**
     * Extracts the client IP address from the request, handling proxies.
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }
}
