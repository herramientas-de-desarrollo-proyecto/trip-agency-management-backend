package com.tripagencymanagement.template.notifications.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tripagencymanagement.template.notifications.domain.entities.DUserNotification;
import com.tripagencymanagement.template.notifications.infrastructure.entities.UserNotification;

@Mapper(componentModel = "spring", uses = {INotificationMapper.class})
public interface IUserNotificationMapper {
    
    @Mapping(target = "notification", source = "notification")
    UserNotification toInfrastructure(DUserNotification domain);
    
    @Mapping(target = "notification", source = "notification")
    DUserNotification toDomain(UserNotification infrastructure);
}
