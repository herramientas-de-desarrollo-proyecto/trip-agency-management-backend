package com.tripagencymanagement.template.auth.application.commands;

import com.tripagencymanagement.template.auth.presentation.dto.LoginRequestDto;

/**
 * Command for user login.
 */
public record LoginCommand(
    LoginRequestDto loginDto,
    String userAgent,
    String ipAddress
) {}
