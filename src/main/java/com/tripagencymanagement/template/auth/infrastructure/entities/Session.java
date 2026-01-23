package com.tripagencymanagement.template.auth.infrastructure.entities;

import java.time.LocalDateTime;

import com.tripagencymanagement.template.general.entities.repositoryEntites.BaseAbstractEntity;
import com.tripagencymanagement.template.users.infrastructure.entities.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * JPA entity for user sessions.
 * Stores authentication session data including JWT tokens and metadata.
 */
@Entity
@Table(name = "sessions", indexes = {
    @Index(name = "idx_sessions_token", columnList = "token"),
    @Index(name = "idx_sessions_refresh_token", columnList = "refresh_token"),
    @Index(name = "idx_sessions_user_id", columnList = "user_id"),
    @Index(name = "idx_sessions_expires_at", columnList = "expires_at")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Session extends BaseAbstractEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(nullable = false, length = 512, unique = true)
    private String token;

    @Column(name = "refresh_token", nullable = false, length = 512, unique = true)
    private String refreshToken;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "refresh_expires_at", nullable = false)
    private LocalDateTime refreshExpiresAt;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @lombok.Builder.Default
    @Column(name = "is_revoked", nullable = false)
    private Boolean isRevoked = false;
}
