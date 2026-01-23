package com.tripagencymanagement.template.auth.infrastructure.mappers;

import org.springframework.stereotype.Component;

import com.tripagencymanagement.template.auth.domain.entities.DSession;
import com.tripagencymanagement.template.auth.infrastructure.entities.Session;

/**
 * Implementation of Session mapper for domain ↔ persistence conversions.
 */
@Component
public class SessionMapper implements ISessionMapper {

    @Override
    public Session toPersistence(DSession domainSession) {
        if (domainSession == null) {
            return null;
        }

        Session.SessionBuilder<?, ?> builder = Session.builder();
        
        builder.userId(domainSession.getUserId());
        builder.token(domainSession.getToken());
        builder.refreshToken(domainSession.getRefreshToken());
        builder.expiresAt(domainSession.getExpiresAt());
        builder.refreshExpiresAt(domainSession.getRefreshExpiresAt());
        builder.userAgent(mapOptionalStringToString(domainSession.getUserAgent()));
        builder.ipAddress(mapOptionalStringToString(domainSession.getIpAddress()));
        builder.isRevoked(domainSession.getIsRevoked());

        if (domainSession.getId() != null) {
            builder.id(domainSession.getId());
        }
        if (domainSession.getIsActive() != null) {
            builder.isActive(domainSession.getIsActive());
        }
        if (domainSession.getCreatedDate() != null) {
            builder.createdDate(domainSession.getCreatedDate());
        }
        if (domainSession.getUpdatedDate() != null) {
            builder.updatedDate(domainSession.getUpdatedDate());
        }

        return builder.build();
    }

    @Override
    public DSession toDomain(Session persistenceSession) {
        if (persistenceSession == null) {
            return null;
        }

        DSession domainSession = new DSession();
        domainSession.setId(persistenceSession.getId());
        domainSession.setUserId(persistenceSession.getUserId());
        domainSession.setToken(persistenceSession.getToken());
        domainSession.setRefreshToken(persistenceSession.getRefreshToken());
        domainSession.setExpiresAt(persistenceSession.getExpiresAt());
        domainSession.setRefreshExpiresAt(persistenceSession.getRefreshExpiresAt());
        domainSession.setUserAgent(mapStringToOptionalString(persistenceSession.getUserAgent()));
        domainSession.setIpAddress(mapStringToOptionalString(persistenceSession.getIpAddress()));
        domainSession.setIsRevoked(persistenceSession.getIsRevoked());
        domainSession.setIsActive(persistenceSession.getIsActive());
        domainSession.setCreatedDate(persistenceSession.getCreatedDate());
        domainSession.setUpdatedDate(persistenceSession.getUpdatedDate());

        return domainSession;
    }
}
