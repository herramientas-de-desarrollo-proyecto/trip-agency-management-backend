package com.tripagencymanagement.template.users.application.commands;

import com.tripagencymanagement.template.users.presentation.dto.UpdateUserDto;

public record UpdateUserCommand(Long userId, UpdateUserDto dto) {
}
