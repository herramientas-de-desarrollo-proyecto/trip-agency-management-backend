package com.tripagencymanagement.template.users.application.commands;

import com.tripagencymanagement.template.users.presentation.dto.CreateUserDto;

public record CreateUserCommand(CreateUserDto userDto) {
}
