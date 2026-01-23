package com.tripagencymanagement.template.auth.application.queries.handlers;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.auth.application.queries.GetUserSessionsQuery;
import com.tripagencymanagement.template.auth.domain.entities.DSession;
import com.tripagencymanagement.template.auth.domain.repositories.ISessionRepository;
import com.tripagencymanagement.template.auth.presentation.dto.SessionInfoDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler for getting user sessions query.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GetUserSessionsQueryHandler {

    private final ISessionRepository sessionRepository;

    public List<SessionInfoDto> execute(GetUserSessionsQuery query, String currentToken) {
        log.debug("Getting active sessions for user ID: {}", query.userId());

        List<DSession> sessions = sessionRepository.findActiveSessionsByUserId(query.userId());

        return sessions.stream()
                .map(session -> SessionInfoDto.builder()
                        .id(session.getId())
                        .createdAt(session.getCreatedDate())
                        .expiresAt(session.getExpiresAt())
                        .userAgent(session.getUserAgent().orElse(null))
                        .ipAddress(session.getIpAddress().orElse(null))
                        .isCurrent(session.getToken().equals(currentToken))
                        .isActive(session.getIsActive())
                        .build())
                .toList();
    }
}
