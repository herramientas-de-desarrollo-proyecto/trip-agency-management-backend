package com.tripagencymanagement.template.auth.domain.entities;

import java.time.LocalDateTime;
import java.util.Optional;

import com.tripagencymanagement.template.general.entities.domainEntities.BaseAbstractDomainEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Domain entity representing a user session.
 * A session tracks user authentication state and allows session management.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DSession extends BaseAbstractDomainEntity {
    
    private Long userId;
    private String token;
    private String refreshToken;
    private LocalDateTime expiresAt;
    private LocalDateTime refreshExpiresAt;
    private Optional<String> userAgent = Optional.empty();
    private Optional<String> ipAddress = Optional.empty();
    private Boolean isRevoked = false;

    /**
     * Checks if the session is expired
     * @return true if the session has expired
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    /**
     * Checks if the refresh token is expired
     * @return true if the refresh token has expired
     */
    public boolean isRefreshExpired() {
        return LocalDateTime.now().isAfter(refreshExpiresAt);
    }

    /**
     * Checks if the session is valid (not expired and not revoked)
     * @return true if the session is valid
     */
    public boolean isValid() {
        return !isExpired() && !isRevoked && getIsActive();
    }

    /**
     * Checks if the session can be refreshed
     * @return true if the session can be refreshed
     */
    public boolean canRefresh() {
        return !isRefreshExpired() && !isRevoked && getIsActive();
    }

    /**
     * Revokes the session
     */
    public void revoke() {
        this.isRevoked = true;
        this.setIsActive(false);
    }

    @Override
    public String toString() {
        return "DSession{" +
                "id=" + getId() +
                ", userId=" + userId +
                ", expiresAt=" + expiresAt +
                ", isRevoked=" + isRevoked +
                ", isActive=" + getIsActive() +
                '}';
    }
}
