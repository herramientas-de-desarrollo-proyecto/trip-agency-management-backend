package com.tripagencymanagement.template.auth.application.commands;

import com.tripagencymanagement.template.auth.presentation.dto.ChangePasswordRequestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Command for changing user password.
 */
@Getter
@AllArgsConstructor
public class ChangePasswordCommand {
    private final Long userId;
    private final ChangePasswordRequestDto request;
}
