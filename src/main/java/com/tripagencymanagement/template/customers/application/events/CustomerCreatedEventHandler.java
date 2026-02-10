package com.tripagency.ptc.ptcagencydemo.customers.application.events;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.tripagency.ptc.ptcagencydemo.notifications.application.services.NotificationService;
import com.tripagency.ptc.ptcagencydemo.notifications.domain.enums.DNotificationScope;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerCreatedEventHandler {
    
    private final NotificationService notificationService;

    @Autowired
    public CustomerCreatedEventHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    public void handleCustomerCreated(CustomerCreatedDomainEvent event) {
        log.info("Handling CustomerCreatedDomainEvent for customer: {}", event.customerId());
        
        // Enviar notificación a todos los usuarios
        notificationService.sendNotification(
                event.message(),
                DNotificationScope.ALL,
                Optional.of(event.customerId().toString()),
                null  // No hay usuario específico que generó la acción en este caso
        );
        
        log.info("Notification sent for customer created: {}", event.customerId());
    }
}

