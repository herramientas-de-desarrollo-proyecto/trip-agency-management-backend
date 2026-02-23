package com.tripagencymanagement.template.notifications.application.commands.handlers;

import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.notifications.application.commands.MarkNotificationAsReadCommand;
import com.tripagencymanagement.template.notifications.domain.entities.DUserNotification;
import com.tripagencymanagement.template.notifications.domain.repositories.IUserNotificationRepository;

@Service
public class MarkNotificationAsReadCommandHandler {
    
    private final IUserNotificationRepository userNotificationRepository;

    public MarkNotificationAsReadCommandHandler(IUserNotificationRepository userNotificationRepository) {
        this.userNotificationRepository = userNotificationRepository;
    }

    public DUserNotification execute(MarkNotificationAsReadCommand command) {
        DUserNotification userNotification = userNotificationRepository.findById(command.userNotificationId())
                .orElseThrow(() -> new IllegalArgumentException("User notification not found"));
        
        userNotification.markAsRead();
        return userNotificationRepository.save(userNotification);
    }
}
