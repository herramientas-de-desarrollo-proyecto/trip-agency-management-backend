package com.tripagencymanagement.template.notifications.infrastructure.repositories.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tripagencymanagement.template.notifications.domain.entities.DNotification;
import com.tripagencymanagement.template.notifications.domain.repositories.INotificationRepository;
import com.tripagencymanagement.template.notifications.infrastructure.mappers.INotificationMapper;
import com.tripagencymanagement.template.notifications.infrastructure.repositories.interfaces.INotificationJpaRepository;

@Repository
public class NotificationRepository implements INotificationRepository {

    private final INotificationJpaRepository jpaRepository;
    private final INotificationMapper mapper;

    public NotificationRepository(INotificationJpaRepository jpaRepository, INotificationMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public DNotification save(DNotification notification) {
        var infrastructureEntity = mapper.toInfrastructure(notification);
        var savedEntity = jpaRepository.save(infrastructureEntity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<DNotification> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void deleteByCreatedDateBefore(LocalDateTime dateTime) {
        jpaRepository.deleteByCreatedDateBefore(dateTime);
    }
}
