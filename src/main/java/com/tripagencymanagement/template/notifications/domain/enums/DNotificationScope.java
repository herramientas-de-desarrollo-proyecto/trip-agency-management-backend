package com.tripagencymanagement.template.notifications.domain.enums;

public enum DNotificationScope {
    ALL,      // Notificación para todos los usuarios
    SELF,     // Notificación solo para el usuario que generó la acción
    OTHERS    // Notificación para todos excepto el usuario que generó la acción
}
