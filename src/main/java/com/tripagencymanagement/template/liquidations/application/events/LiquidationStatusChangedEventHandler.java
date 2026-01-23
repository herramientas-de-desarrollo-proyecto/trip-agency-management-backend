package com.tripagencymanagement.template.liquidations.application.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.tripagencymanagement.template.notifications.application.services.NotificationService;
import com.tripagencymanagement.template.notifications.domain.enums.DNotificationScope;
import com.tripagencymanagement.template.notifications.domain.enums.DNotificationType;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LiquidationStatusChangedEventHandler {
    
    private final NotificationService notificationService;

    public LiquidationStatusChangedEventHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    public void handleLiquidationStatusChanged(LiquidationStatusChangedDomainEvent event) {
        log.info("Handling LiquidationStatusChangedDomainEvent for liquidation: {}", event.liquidationId());
        
        String title = "Estado Actualizado";
        String message = String.format(
            "La liquidación %s cambió de estado: %s → %s",
            event.file(),
            translateStatus(event.oldStatus()),
            translateStatus(event.newStatus())
        );
        
        notificationService.sendNotification(
            title,
            message,
            DNotificationType.LIQUIDATION_STATUS_UPDATED,
            DNotificationScope.OTHERS,
            event.liquidationId().toString(),
            "LIQUIDATION",
            event.triggerUserId()
        );
        
        log.info("Notification sent for liquidation status changed: {}", event.liquidationId());
    }
    
    private String translateStatus(String status) {
        return switch (status) {
            case "IN_QUOTE" -> "En Cotización";
            case "PENDING" -> "Pendiente";
            case "PAID" -> "Pagada";
            case "CANCELLED" -> "Cancelada";
            default -> status;
        };
    }
}
