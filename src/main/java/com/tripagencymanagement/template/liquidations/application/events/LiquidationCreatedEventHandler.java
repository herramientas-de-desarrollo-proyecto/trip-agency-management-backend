package com.tripagencymanagement.template.liquidations.application.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.tripagencymanagement.template.notifications.application.services.NotificationService;
import com.tripagencymanagement.template.notifications.domain.enums.DNotificationScope;
import com.tripagencymanagement.template.notifications.domain.enums.DNotificationType;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LiquidationCreatedEventHandler {
    
    private final NotificationService notificationService;

    public LiquidationCreatedEventHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    public void handleLiquidationCreated(LiquidationCreatedDomainEvent event) {
        log.info("Handling LiquidationCreatedDomainEvent for liquidation: {}", event.liquidationId());
        
        String title = "Nueva Liquidación";
        String message = String.format(
            "Se creó la liquidación %s para el cliente %s",
            event.file(),
            event.customerName()
        );
        
        notificationService.sendNotification(
            title,
            message,
            DNotificationType.LIQUIDATION_CREATED,
            DNotificationScope.ALL,
            event.liquidationId().toString(),
            "LIQUIDATION",
            event.triggerUserId()
        );
        
        log.info("Notification sent for liquidation created: {}", event.liquidationId());
    }
}
