package com.tripagencymanagement.template.auth.application.commands.handlers;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.auth.application.commands.LoginCommand;
import com.tripagencymanagement.template.auth.application.events.UserLoggedInEvent;
import com.tripagencymanagement.template.auth.application.services.JwtService;
import com.tripagencymanagement.template.auth.domain.entities.DAuthenticatedUser;
import com.tripagencymanagement.template.auth.domain.entities.DSession;
import com.tripagencymanagement.template.auth.domain.repositories.ISessionRepository;
import com.tripagencymanagement.template.auth.presentation.dto.AuthResponseDto;
import com.tripagencymanagement.template.general.utils.exceptions.HtpExceptionUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler for login command.
 * Authenticates user and creates a new session with JWT tokens.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LoginCommandHandler {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ISessionRepository sessionRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public AuthResponseDto execute(LoginCommand command) {
        try {
            log.info("Attempting login for user: {}", command.loginDto().getEmail());

            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            command.loginDto().getEmail(),
                            command.loginDto().getPassword()
                    )
            );

            DAuthenticatedUser authenticatedUser = (DAuthenticatedUser) authentication.getPrincipal();

            // Generate tokens
            String accessToken = jwtService.generateAccessToken(authenticatedUser);
            String refreshToken = jwtService.generateRefreshToken(authenticatedUser);

            // Calculate expiration times
            LocalDateTime accessTokenExpiresAt = LocalDateTime.now()
                    .plusSeconds(jwtService.getAccessTokenExpiration() / 1000);
            LocalDateTime refreshTokenExpiresAt = LocalDateTime.now()
                    .plusSeconds(jwtService.getRefreshTokenExpiration() / 1000);

            // Create session
            DSession session = new DSession();
            session.setUserId(authenticatedUser.getUserId());
            session.setToken(accessToken);
            session.setRefreshToken(refreshToken);
            session.setExpiresAt(accessTokenExpiresAt);
            session.setRefreshExpiresAt(refreshTokenExpiresAt);
            session.setUserAgent(Optional.ofNullable(command.userAgent()));
            session.setIpAddress(Optional.ofNullable(command.ipAddress()));
            session.setIsRevoked(false);
            session.setIsActive(true);

            // Save session
            sessionRepository.save(session);

            log.info("User logged in successfully: {}", authenticatedUser.getEmail());

            // Publish event
            eventPublisher.publishEvent(new UserLoggedInEvent(
                    authenticatedUser.getUserId(),
                    authenticatedUser.getEmail(),
                    command.ipAddress()
            ));

            // Build response
            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtService.getAccessTokenExpiration() / 1000)
                    .refreshExpiresIn(jwtService.getRefreshTokenExpiration() / 1000)
                    .expiresAt(accessTokenExpiresAt)
                    .user(AuthResponseDto.UserInfoDto.builder()
                            .id(authenticatedUser.getUserId())
                            .email(authenticatedUser.getEmail())
                            .userName(authenticatedUser.getUserName().orElse(null))
                            .isActive(authenticatedUser.getUser().getIsActive())
                            .build())
                    .build();

        } catch (BadCredentialsException e) {
            log.warn("Login failed for user: {} - Invalid credentials", command.loginDto().getEmail());
            throw HtpExceptionUtils.processHttpException(
                    new IllegalArgumentException("Credenciales inválidas")
            );
        } catch (Exception e) {
            log.error("Login failed for user: {} - {}", command.loginDto().getEmail(), e.getMessage());
            throw HtpExceptionUtils.processHttpException(e);
        }
    }
}
