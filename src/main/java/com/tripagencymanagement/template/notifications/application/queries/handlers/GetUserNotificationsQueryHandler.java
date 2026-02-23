package com.tripagencymanagement.template.notifications.application.queries.handlers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.notifications.application.queries.GetUserNotificationsQuery;
import com.tripagencymanagement.template.notifications.domain.entities.DUserNotification;
import com.tripagencymanagement.template.notifications.domain.repositories.IUserNotificationRepository;

@Service
public class GetUserNotificationsQueryHandler {
    
    private final IUserNotificationRepository userNotificationRepository;

    public GetUserNotificationsQueryHandler(IUserNotificationRepository userNotificationRepository) {
        this.userNotificationRepository = userNotificationRepository;
    }

    public Page<DUserNotification> execute(GetUserNotificationsQuery query) {
        Pageable pageable = PageRequest.of(
            query.requestDto().getPage(),
            query.requestDto().getSize(),
            Sort.by(Sort.Direction.DESC, "createdDate")
        );
        return userNotificationRepository.findByUserId(query.userId(), pageable);
    }
}
