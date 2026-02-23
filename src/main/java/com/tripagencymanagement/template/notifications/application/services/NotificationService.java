package com.tripagencymanagement.template.notifications.application.services;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.tripagencymanagement.template.notifications.domain.entities.DNotification;
import com.tripagencymanagement.template.notifications.domain.entities.DUserNotification;
import com.tripagencymanagement.template.notifications.domain.enums.DNotificationScope;
import com.tripagencymanagement.template.notifications.domain.enums.DNotificationType;
import com.tripagencymanagement.template.notifications.domain.repositories.INotificationRepository;
import com.tripagencymanagement.template.notifications.domain.repositories.IUserNotificationRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationService {

    private final INotificationRepository notificationRepository;
    private final IUserNotificationRepository userNotificationRepository;
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public NotificationService(
            INotificationRepository notificationRepository,
            IUserNotificationRepository userNotificationRepository) {
        this.notificationRepository = notificationRepository;
        this.userNotificationRepository = userNotificationRepository;
    }

    /**
     * Registra un SSE emitter para un usuario
     */
    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        
        emitter.onCompletion(() -> {
            log.info("SSE completado para el usuario: {}", userId);
            emitters.remove(userId);
        });
        
        emitter.onTimeout(() -> {
            log.info("SSE timeout para el usuario: {}", userId);
            emitters.remove(userId);
        });
        
        emitter.onError((ex) -> {
            log.error("SSE error para el usuario: {}", userId, ex);
            emitters.remove(userId);
        });
        
        emitters.put(userId, emitter);
        log.info("Usuario {} suscrito a notificaciones", userId);
        
        return emitter;
    }

    /**
     * Envía una notificación completa con todos los campos
     * @param title Título de la notificación
     * @param message Mensaje de la notificación
     * @param type Tipo de notificación
     * @param scope Alcance de la notificación (ALL, SELF, OTHERS)
     * @param referenceId ID de referencia opcional (ej: liquidationId)
     * @param referenceType Tipo de referencia (ej: "LIQUIDATION", "CUSTOMER")
     * @param triggerUserId ID del usuario que generó la acción
     */
    public void sendNotification(
            String title,
            String message, 
            DNotificationType type,
            DNotificationScope scope, 
            String referenceId, 
            String referenceType,
            Long triggerUserId) {
        // Guardar la notificación en la base de datos
        DNotification notification = new DNotification(title, message, type, scope, referenceId, referenceType);
        DNotification savedNotification = notificationRepository.save(notification);
        
        // Determinar a qué usuarios enviar
        switch (scope) {
            case ALL:
                sendToAllUsers(savedNotification);
                break;
            case SELF:
                sendToUser(savedNotification, triggerUserId);
                break;
            case OTHERS:
                sendToOthers(savedNotification, triggerUserId);
                break;
        }
    }

    /**
     * Envía una notificación según el scope (método de compatibilidad)
     * @param message Mensaje de la notificación
     * @param scope Alcance de la notificación (ALL, SELF, OTHERS)
     * @param referenceId ID de referencia opcional (ej: liquidationId)
     * @param triggerUserId ID del usuario que generó la acción
     */
    public void sendNotification(String message, DNotificationScope scope, Optional<String> referenceId, Long triggerUserId) {
        sendNotification(
            "Notificación",
            message,
            DNotificationType.SYSTEM_INFO,
            scope,
            referenceId.orElse(null),
            null,
            triggerUserId
        );
    }

    private void sendToAllUsers(DNotification notification) {
        emitters.forEach((userId, emitter) -> {
            saveUserNotification(userId, notification);
            sendToEmitter(emitter, notification, userId);
        });
    }

    private void sendToUser(DNotification notification, Long userId) {
        saveUserNotification(userId, notification);
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            sendToEmitter(emitter, notification, userId);
        }
    }

    private void sendToOthers(DNotification notification, Long excludeUserId) {
        emitters.forEach((userId, emitter) -> {
            if (!userId.equals(excludeUserId)) {
                saveUserNotification(userId, notification);
                sendToEmitter(emitter, notification, userId);
            }
        });
    }

    private void saveUserNotification(Long userId, DNotification notification) {
        DUserNotification userNotification = new DUserNotification(
            userId,
            notification.getId(),
            notification
        );
        userNotificationRepository.save(userNotification);
    }

    private void sendToEmitter(SseEmitter emitter, DNotification notification, Long userId) {
        try {
            emitter.send(SseEmitter.event()
                    .name("notification")
                    .data(notification));
            log.info("Notificación enviada al usuario: {}", userId);
        } catch (Exception e) {
            log.error("Error al enviar notificación al usuario: {}", userId, e);
            emitters.remove(userId);
        }
    }

    /**
     * Desuscribe a un usuario de las notificaciones
     */
    public void unsubscribe(Long userId) {
        SseEmitter emitter = emitters.remove(userId);
        if (emitter != null) {
            emitter.complete();
            log.info("Usuario {} desuscrito de notificaciones", userId);
        }
    }
}
