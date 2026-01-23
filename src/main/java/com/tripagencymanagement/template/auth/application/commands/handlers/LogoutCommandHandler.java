package com.tripagencymanagement.template.auth.application.commands.handlers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.auth.application.commands.LogoutCommand;
import com.tripagencymanagement.template.auth.application.events.UserLoggedOutEvent;
import com.tripagencymanagement.template.auth.domain.repositories.ISessionRepository;
import com.tripagencymanagement.template.auth.presentation.dto.LogoutResponseDto;
import com.tripagencymanagement.template.general.utils.exceptions.HtpExceptionUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler for logout command.
 * Revokes the current session.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutCommandHandler {

    private final ISessionRepository sessionRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public LogoutResponseDto execute(LogoutCommand command) {
        try {
            log.info("Processing logout for user ID: {}", command.userId());

            // Find and revoke session by token
            sessionRepository.findByToken(command.accessToken())
                    .ifPresent(session -> {
                        session.revoke();
                        sessionRepository.update(session);
                        log.info("Session revoked for user ID: {}", command.userId());
                    });

            // Publish event
            eventPublisher.publishEvent(new UserLoggedOutEvent(command.userId()));

            return LogoutResponseDto.builder()
                    .message("Sesión cerrada exitosamente")
                    .success(true)
                    .build();

        } catch (Exception e) {
            log.error("Logout failed for user ID: {} - {}", command.userId(), e.getMessage());
            throw HtpExceptionUtils.processHttpException(e);
        }
    }
}
