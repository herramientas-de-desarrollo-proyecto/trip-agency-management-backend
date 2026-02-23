package com.tripagencymanagement.template.notifications.presentation.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.tripagencymanagement.template.notifications.application.commands.MarkNotificationAsReadCommand;
import com.tripagencymanagement.template.notifications.application.commands.handlers.MarkNotificationAsReadCommandHandler;
import com.tripagencymanagement.template.notifications.application.queries.GetUserNotificationsQuery;
import com.tripagencymanagement.template.notifications.application.queries.handlers.GetUserNotificationsQueryHandler;
import com.tripagencymanagement.template.notifications.application.services.NotificationService;
import com.tripagencymanagement.template.notifications.domain.entities.DUserNotification;
import com.tripagencymanagement.template.notifications.presentation.dto.PaginatedNotificationRequestDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/notifications")
@Tag(name = "Notificaciones", description = "Endpoints para la gestión de notificaciones")
public class NotificationController {

    private final NotificationService notificationService;
    private final GetUserNotificationsQueryHandler getUserNotificationsQueryHandler;
    private final MarkNotificationAsReadCommandHandler markNotificationAsReadCommandHandler;

    public NotificationController(
            NotificationService notificationService,
            GetUserNotificationsQueryHandler getUserNotificationsQueryHandler,
            MarkNotificationAsReadCommandHandler markNotificationAsReadCommandHandler) {
        this.notificationService = notificationService;
        this.getUserNotificationsQueryHandler = getUserNotificationsQueryHandler;
        this.markNotificationAsReadCommandHandler = markNotificationAsReadCommandHandler;
    }

    @GetMapping(value = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Suscribirse a notificaciones en tiempo real a través de SSE")
    public SseEmitter subscribe(@PathVariable Long userId) {
        return notificationService.subscribe(userId);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener notificaciones paginadas para un usuario")
    public Page<DUserNotification> getUserNotifications(
            @PathVariable Long userId,
            @ModelAttribute PaginatedNotificationRequestDto requestDto) {
        requestDto.normalizePageNumber();
        GetUserNotificationsQuery query = new GetUserNotificationsQuery(userId, requestDto);
        return getUserNotificationsQueryHandler.execute(query);
    }

    @PutMapping("/{userNotificationId}/mark-as-read")
    @Operation(summary = "Marcar una notificación como leída")
    public ResponseEntity<DUserNotification> markAsRead(@PathVariable Long userNotificationId) {
        MarkNotificationAsReadCommand command = new MarkNotificationAsReadCommand(userNotificationId);
        DUserNotification userNotification = markNotificationAsReadCommandHandler.execute(command);
        return ResponseEntity.ok(userNotification);
    }
}
