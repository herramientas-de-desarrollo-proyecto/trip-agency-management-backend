package com.tripagencymanagement.template.auth.application.events;

/**
 * Domain event published when a user logs in.
 */
public record UserLoggedInEvent(
    Long userId,
    String email,
    String ipAddress
) {}
