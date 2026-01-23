package com.tripagencymanagement.template.auth.application.queries;

/**
 * Query for getting current user information.
 */
public record GetCurrentUserQuery(
    Long userId
) {}
