package com.tripagencymanagement.template.auth.infrastructure.repositories.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.tripagencymanagement.template.auth.domain.entities.DSession;
import com.tripagencymanagement.template.auth.domain.repositories.ISessionRepository;
import com.tripagencymanagement.template.auth.infrastructure.entities.Session;
import com.tripagencymanagement.template.auth.infrastructure.mappers.SessionMapper;
import com.tripagencymanagement.template.auth.infrastructure.repositories.interfaces.ISessionJpaRepository;

import jakarta.transaction.Transactional;

/**
 * Implementation of the session repository using JPA.
 */
@Repository
public class SessionRepository implements ISessionRepository {
    
    private final ISessionJpaRepository jpaRepository;
    private final SessionMapper mapper;

    public SessionRepository(ISessionJpaRepository jpaRepository, SessionMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public DSession save(DSession session) {
        Session entity = mapper.toPersistence(session);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public DSession update(DSession session) {
        Session entity = mapper.toPersistence(session);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<DSession> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<DSession> findByToken(String token) {
        return jpaRepository.findByToken(token).map(mapper::toDomain);
    }

    @Override
    public Optional<DSession> findByRefreshToken(String refreshToken) {
        return jpaRepository.findByRefreshToken(refreshToken).map(mapper::toDomain);
    }

    @Override
    public List<DSession> findActiveSessionsByUserId(Long userId) {
        return jpaRepository.findActiveSessionsByUserId(userId, LocalDateTime.now())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<DSession> findAllByUserId(Long userId) {
        return jpaRepository.findAllByUserId(userId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllByUserId(Long userId) {
        jpaRepository.deleteAllByUserId(userId);
    }

    @Override
    @Transactional
    public void revokeAllUserSessions(Long userId) {
        jpaRepository.revokeAllUserSessions(userId);
    }

    @Override
    @Transactional
    public void revokeSession(Long sessionId) {
        jpaRepository.revokeSession(sessionId);
    }

    @Override
    @Transactional
    public int deleteExpiredSessions() {
        return jpaRepository.deleteExpiredSessions(LocalDateTime.now());
    }
}
