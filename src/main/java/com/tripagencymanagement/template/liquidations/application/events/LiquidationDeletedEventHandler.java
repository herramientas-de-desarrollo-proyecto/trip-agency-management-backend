package com.tripagencymanagement.template.liquidations.application.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.tripagencymanagement.template.notifications.application.services.NotificationService;
import com.tripagencymanagement.template.notifications.domain.enums.DNotificationScope;
import com.tripagencymanagement.template.notifications.domain.enums.DNotificationType;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LiquidationDeletedEventHandler {
    
    private final NotificationService notificationService;

    public LiquidationDeletedEventHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    public void handleLiquidationDeleted(LiquidationDeletedDomainEvent event) {
        log.info("Handling LiquidationDeletedDomainEvent for liquidation: {}", event.liquidationId());
        
        String title = "Liquidación Eliminada";
        String message = String.format("Se eliminó la liquidación %s", event.file());
        
        notificationService.sendNotification(
            title,
            message,
            DNotificationType.LIQUIDATION_DELETED,
            DNotificationScope.OTHERS,
            event.liquidationId().toString(),
            "LIQUIDATION",
            event.triggerUserId()
        );
        
        log.info("Notification sent for liquidation deleted: {}", event.liquidationId());
    }
}
