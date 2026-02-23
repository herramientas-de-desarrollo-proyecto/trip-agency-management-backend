package com.tripagencymanagement.template.notifications.infrastructure.mappers;

import org.mapstruct.Mapper;

import com.tripagencymanagement.template.notifications.domain.enums.DNotificationScope;
import com.tripagencymanagement.template.notifications.domain.enums.DNotificationType;
import com.tripagencymanagement.template.notifications.infrastructure.enums.NotificationScope;
import com.tripagencymanagement.template.notifications.infrastructure.enums.NotificationType;

@Mapper(componentModel = "spring")
public interface INotificationEnumMapper {
    
    NotificationScope toInfrastructure(DNotificationScope domain);
    
    DNotificationScope toDomain(NotificationScope infrastructure);
    
    NotificationType toInfrastructure(DNotificationType domain);
    
    DNotificationType toDomain(NotificationType infrastructure);
}
