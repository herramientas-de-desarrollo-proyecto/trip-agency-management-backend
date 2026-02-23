package com.tripagencymanagement.template.notifications.infrastructure.repositories.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tripagencymanagement.template.notifications.domain.entities.DUserNotification;
import com.tripagencymanagement.template.notifications.domain.repositories.IUserNotificationRepository;
import com.tripagencymanagement.template.notifications.infrastructure.mappers.IUserNotificationMapper;
import com.tripagencymanagement.template.notifications.infrastructure.repositories.interfaces.IUserNotificationJpaRepository;

@Component
public class UserNotificationRepository implements IUserNotificationRepository {

    private final IUserNotificationJpaRepository jpaRepository;
    private final IUserNotificationMapper mapper;

    public UserNotificationRepository(IUserNotificationJpaRepository jpaRepository, IUserNotificationMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public DUserNotification save(DUserNotification userNotification) {
        var infrastructureEntity = mapper.toInfrastructure(userNotification);
        var savedEntity = jpaRepository.save(infrastructureEntity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<DUserNotification> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Page<DUserNotification> findByUserId(Long userId, Pageable pageable) {
        return jpaRepository.findByUserId(userId, pageable)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void deleteByNotificationId(Long notificationId) {
        jpaRepository.deleteByNotificationId(notificationId);
    }
}
