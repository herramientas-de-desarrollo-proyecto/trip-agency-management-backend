package com.tripagencymanagement.template.notifications.infrastructure.entities;

import com.tripagencymanagement.template.general.entities.repositoryEntites.BaseAbstractEntity;
import com.tripagencymanagement.template.notifications.infrastructure.enums.NotificationScope;
import com.tripagencymanagement.template.notifications.infrastructure.enums.NotificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
public class Notification extends BaseAbstractEntity {
    
    @Column(length = 200)
    private String title;
    
    @Column(nullable = false, length = 500)
    private String message;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationScope scope;
    
    @Column(name = "reference_id")
    private String referenceId;
    
    @Column(name = "reference_type", length = 50)
    private String referenceType;
}
