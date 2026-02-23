package com.tripagencymanagement.template.notifications.domain.enums;

/**
 * Tipo de notificación para categorizar los eventos
 */
public enum DNotificationType {
    // Liquidaciones
    LIQUIDATION_CREATED,
    LIQUIDATION_STATUS_UPDATED,
    LIQUIDATION_PAYMENT_STATUS_UPDATED,
    LIQUIDATION_DELETED,
    
    // Pagos
    PAYMENT_ADDED,
    PAYMENT_UPDATED,
    PAYMENT_DELETED,
    
    // Servicios
    SERVICE_ADDED,
    SERVICE_UPDATED,
    SERVICE_DELETED,
    
    // Incidencias
    INCIDENCY_ADDED,
    INCIDENCY_UPDATED,
    INCIDENCY_DELETED,
    
    // Clientes
    CUSTOMER_CREATED,
    CUSTOMER_UPDATED,
    CUSTOMER_DELETED,
    
    // Staff
    STAFF_CREATED,
    STAFF_UPDATED,
    STAFF_DELETED,
    
    // Usuarios
    USER_CREATED,
    USER_UPDATED,
    USER_DELETED,
    
    // Sistema
    SYSTEM_INFO,
    SYSTEM_WARNING,
    SYSTEM_ERROR
}
