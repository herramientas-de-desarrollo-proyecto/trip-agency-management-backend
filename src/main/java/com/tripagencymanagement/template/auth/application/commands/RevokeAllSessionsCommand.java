package com.tripagencymanagement.template.auth.application.commands;

/**
 * Command for revoking all user sessions.
 */
public record RevokeAllSessionsCommand(
    Long userId
) {}
