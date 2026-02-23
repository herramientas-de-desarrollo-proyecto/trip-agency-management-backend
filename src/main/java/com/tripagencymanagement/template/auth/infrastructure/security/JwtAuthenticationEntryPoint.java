package com.tripagencymanagement.template.auth.infrastructure.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripagencymanagement.template.general.presentation.exception.ErrorBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom authentication entry point that handles unauthorized access attempts.
 * Returns a JSON error response for unauthenticated requests.
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        log.error("Unauthorized error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorBody errorBody = new ErrorBody(
                "No autorizado - Token inválido o expirado",
                HttpStatus.UNAUTHORIZED,
                Optional.of(authException.getMessage()),
                Optional.empty()
        );

        objectMapper.writeValue(response.getOutputStream(), errorBody);
    }
}
