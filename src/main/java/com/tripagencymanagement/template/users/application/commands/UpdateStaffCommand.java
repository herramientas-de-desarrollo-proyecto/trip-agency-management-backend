package com.tripagencymanagement.template.users.application.commands;

import com.tripagencymanagement.template.users.presentation.dto.UpdateStaffDto;

public record UpdateStaffCommand(Long staffId, UpdateStaffDto dto) {
}
