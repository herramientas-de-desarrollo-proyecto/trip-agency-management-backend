package com.tripagencymanagement.template.liquidations.application.commands;

import com.tripagencymanagement.template.liquidations.presentation.dto.UpdateAdditionalServiceDto;

public record UpdateAdditionalServiceCommand(
    Long liquidationId,
    Long additionalServiceId,
    UpdateAdditionalServiceDto additionalServiceDto
) {}
