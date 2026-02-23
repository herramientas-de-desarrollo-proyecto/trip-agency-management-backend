package com.tripagencymanagement.template.notifications.domain.entities;

import java.util.Optional;

import com.tripagencymanagement.template.general.entities.domainEntities.BaseAbstractDomainEntity;
import com.tripagencymanagement.template.notifications.domain.enums.DNotificationScope;
import com.tripagencymanagement.template.notifications.domain.enums.DNotificationType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DNotification extends BaseAbstractDomainEntity {
    
    private String title;
    private String message;
    private DNotificationType type;
    private DNotificationScope scope;
    private String referenceId;
    private String referenceType; // "LIQUIDATION", "CUSTOMER", "STAFF", etc.

    public DNotification(String title, String message, DNotificationType type, DNotificationScope scope, String referenceId, String referenceType) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.scope = scope;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        validateNotification();
    }

    // Constructor de compatibilidad con el código existente
    public DNotification(String message, DNotificationScope scope, Optional<String> referenceId) {
        this.title = "Notificación";
        this.message = message;
        this.type = DNotificationType.SYSTEM_INFO;
        this.scope = scope;
        this.referenceId = referenceId.orElse(null);
        this.referenceType = null;
        validateNotification();
    }

    private void validateNotification() {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje de la notificación no puede ser nulo o vacío");
        }
        if (scope == null) {
            throw new IllegalArgumentException("El alcance de la notificación no puede ser nulo");
        }
        if (type == null) {
            throw new IllegalArgumentException("El tipo de notificación no puede ser nulo");
        }
    }
}
