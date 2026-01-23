package com.tripagencymanagement.template.liquidations.application.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.tripagencymanagement.template.notifications.application.services.NotificationService;
import com.tripagencymanagement.template.notifications.domain.enums.DNotificationScope;
import com.tripagencymanagement.template.notifications.domain.enums.DNotificationType;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PaymentAddedEventHandler {
    
    private final NotificationService notificationService;

    public PaymentAddedEventHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    public void handlePaymentAdded(PaymentAddedDomainEvent event) {
        log.info("Handling PaymentAddedDomainEvent for liquidation: {}", event.liquidationId());
        
        String title = "Pago Agregado";
        String message = String.format(
            "Se agregó un pago de %.2f %s (%s) a la liquidación %s",
            event.amount(),
            event.currency(),
            event.paymentMethod(),
            event.file()
        );
        
        notificationService.sendNotification(
            title,
            message,
            DNotificationType.PAYMENT_ADDED,
            DNotificationScope.ALL,
            event.liquidationId().toString(),
            "LIQUIDATION",
            event.triggerUserId()
        );
        
        log.info("Notification sent for payment added to liquidation: {}", event.liquidationId());
    }
}
