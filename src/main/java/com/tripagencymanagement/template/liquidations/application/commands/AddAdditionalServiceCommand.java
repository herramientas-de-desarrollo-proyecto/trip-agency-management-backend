package com.tripagencymanagement.template.liquidations.application.commands;

import com.tripagencymanagement.template.liquidations.presentation.dto.AddAdditionalServiceDto;

public record AddAdditionalServiceCommand(
    Long liquidationId,
    AddAdditionalServiceDto additionalServiceDto
) {}
