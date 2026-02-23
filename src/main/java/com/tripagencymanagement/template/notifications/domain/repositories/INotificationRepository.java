package com.tripagencymanagement.template.notifications.domain.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import com.tripagencymanagement.template.notifications.domain.entities.DNotification;

public interface INotificationRepository {
    
    DNotification save(DNotification notification);
    
    Optional<DNotification> findById(Long id);
    
    void deleteByCreatedDateBefore(LocalDateTime dateTime);
}
