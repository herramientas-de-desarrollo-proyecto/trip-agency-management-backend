package com.tripagencymanagement.template.auth.infrastructure.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripagencymanagement.template.general.presentation.exception.ErrorBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom access denied handler for handling authorization failures.
 * Returns a JSON error response when access is denied.
 */
@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        log.error("Access denied error: {}", accessDeniedException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ErrorBody errorBody = new ErrorBody(
                "Acceso denegado - No tienes permisos para acceder a este recurso",
                HttpStatus.FORBIDDEN,
                Optional.of(accessDeniedException.getMessage()),
                Optional.empty()
        );

        objectMapper.writeValue(response.getOutputStream(), errorBody);
    }
}
