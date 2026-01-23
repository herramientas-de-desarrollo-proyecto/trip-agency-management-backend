package com.tripagencymanagement.template.auth.application.events;

/**
 * Domain event published when a user logs out.
 */
public record UserLoggedOutEvent(
    Long userId
) {}
