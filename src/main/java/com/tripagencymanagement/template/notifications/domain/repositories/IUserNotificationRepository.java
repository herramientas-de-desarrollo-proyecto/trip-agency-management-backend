package com.tripagencymanagement.template.notifications.domain.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tripagencymanagement.template.notifications.domain.entities.DUserNotification;

public interface IUserNotificationRepository {
    
    DUserNotification save(DUserNotification userNotification);
    
    Optional<DUserNotification> findById(Long id);
    
    Page<DUserNotification> findByUserId(Long userId, Pageable pageable);
    
    void deleteByNotificationId(Long notificationId);
}
