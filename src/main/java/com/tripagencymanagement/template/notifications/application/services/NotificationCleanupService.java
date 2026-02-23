package com.tripagencymanagement.template.notifications.application.services;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripagencymanagement.template.notifications.domain.repositories.INotificationRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationCleanupService {

    private final INotificationRepository notificationRepository;

    public NotificationCleanupService(INotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Elimina notificaciones más antiguas de 30 días
     * Se ejecuta todos los días a las 2:00 AM
     */
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void cleanupOldNotifications() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        log.info("Comenzando a eliminar notificaciones más antiguas que {}", thirtyDaysAgo);
        
        try {
            notificationRepository.deleteByCreatedDateBefore(thirtyDaysAgo);
            log.info("Eliminación de notificaciones antiguas completada con éxito");
        } catch (Exception e) {
            log.error("Error al eliminar notificaciones antiguas", e);
        }
    }
}
