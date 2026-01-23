package com.tripagencymanagement.template.auth.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.tripagencymanagement.template.auth.domain.entities.DSession;

/**
 * Domain repository interface for session management.
 */
public interface ISessionRepository {
    
    DSession save(DSession session);
    
    DSession update(DSession session);
    
    Optional<DSession> findById(Long id);
    
    Optional<DSession> findByToken(String token);
    
    Optional<DSession> findByRefreshToken(String refreshToken);
    
    List<DSession> findActiveSessionsByUserId(Long userId);
    
    List<DSession> findAllByUserId(Long userId);
    
    void deleteById(Long id);
    
    void deleteAllByUserId(Long userId);
    
    void revokeAllUserSessions(Long userId);
    
    void revokeSession(Long sessionId);
    
    int deleteExpiredSessions();
}
