package com.tripagencymanagement.template.notifications.infrastructure.entities;

import com.tripagencymanagement.template.general.entities.repositoryEntites.BaseAbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_notifications")
@Getter
@Setter
@NoArgsConstructor
public class UserNotification extends BaseAbstractEntity {
    
    @Column(nullable = false)
    private Boolean read = false;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "notification_id", nullable = false)
    private Long notificationId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notification_id", insertable = false, updatable = false)
    private Notification notification;
}
