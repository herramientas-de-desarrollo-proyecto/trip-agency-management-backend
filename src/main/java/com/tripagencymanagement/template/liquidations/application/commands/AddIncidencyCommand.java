package com.tripagencymanagement.template.liquidations.application.commands;

import com.tripagencymanagement.template.liquidations.presentation.dto.AddIncidencyDto;

public record AddIncidencyCommand(
    Long liquidationId,
    AddIncidencyDto incidencyDto
) {}
