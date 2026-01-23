package com.tripagencymanagement.template.auth.infrastructure.repositories.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tripagencymanagement.template.auth.infrastructure.entities.Session;

/**
 * JPA repository for Session entity.
 */
@Repository
public interface ISessionJpaRepository extends JpaRepository<Session, Long> {
    
    Optional<Session> findByToken(String token);
    
    Optional<Session> findByRefreshToken(String refreshToken);
    
    @Query("SELECT s FROM Session s WHERE s.userId = :userId AND s.isRevoked = false AND s.isActive = true AND s.expiresAt > :now")
    List<Session> findActiveSessionsByUserId(@Param("userId") Long userId, @Param("now") LocalDateTime now);
    
    List<Session> findAllByUserId(Long userId);
    
    void deleteAllByUserId(Long userId);
    
    @Modifying
    @Query("UPDATE Session s SET s.isRevoked = true, s.isActive = false WHERE s.userId = :userId")
    void revokeAllUserSessions(@Param("userId") Long userId);
    
    @Modifying
    @Query("UPDATE Session s SET s.isRevoked = true, s.isActive = false WHERE s.id = :sessionId")
    void revokeSession(@Param("sessionId") Long sessionId);
    
    @Modifying
    @Query("DELETE FROM Session s WHERE s.refreshExpiresAt < :now")
    int deleteExpiredSessions(@Param("now") LocalDateTime now);
    
    boolean existsByTokenAndIsRevokedFalse(String token);
}
