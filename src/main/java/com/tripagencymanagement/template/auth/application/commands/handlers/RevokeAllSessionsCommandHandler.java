package com.tripagencymanagement.template.auth.application.commands.handlers;

import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.auth.application.commands.RevokeAllSessionsCommand;
import com.tripagencymanagement.template.auth.domain.repositories.ISessionRepository;
import com.tripagencymanagement.template.auth.presentation.dto.LogoutResponseDto;
import com.tripagencymanagement.template.general.utils.exceptions.HtpExceptionUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler for revoking all sessions command.
 * Revokes all active sessions for a user.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RevokeAllSessionsCommandHandler {

    private final ISessionRepository sessionRepository;

    @Transactional
    public LogoutResponseDto execute(RevokeAllSessionsCommand command) {
        try {
            log.info("Revoking all sessions for user ID: {}", command.userId());

            sessionRepository.revokeAllUserSessions(command.userId());

            log.info("All sessions revoked for user ID: {}", command.userId());

            return LogoutResponseDto.builder()
                    .message("Todas las sesiones han sido cerradas exitosamente")
                    .success(true)
                    .build();

        } catch (Exception e) {
            log.error("Failed to revoke all sessions for user ID: {} - {}", command.userId(), e.getMessage());
            throw HtpExceptionUtils.processHttpException(e);
        }
    }
}
