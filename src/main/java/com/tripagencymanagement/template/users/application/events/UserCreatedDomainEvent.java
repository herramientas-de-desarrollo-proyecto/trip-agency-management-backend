package com.tripagencymanagement.template.users.application.events;

public record UserCreatedDomainEvent(Long userId, String message) {
}
