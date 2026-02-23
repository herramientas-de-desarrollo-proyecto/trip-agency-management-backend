package com.tripagencymanagement.template.notifications.infrastructure.repositories.interfaces;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tripagencymanagement.template.notifications.infrastructure.entities.Notification;

@Repository
public interface INotificationJpaRepository extends JpaRepository<Notification, Long> {
    
    void deleteByCreatedDateBefore(LocalDateTime dateTime);
}
