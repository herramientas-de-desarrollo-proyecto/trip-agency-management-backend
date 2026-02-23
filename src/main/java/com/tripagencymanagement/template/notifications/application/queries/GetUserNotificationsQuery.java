package com.tripagencymanagement.template.notifications.application.queries;

import com.tripagencymanagement.template.notifications.presentation.dto.PaginatedNotificationRequestDto;

public record GetUserNotificationsQuery(Long userId, PaginatedNotificationRequestDto requestDto) {
}
