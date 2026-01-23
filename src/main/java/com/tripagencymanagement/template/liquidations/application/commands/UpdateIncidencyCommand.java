package com.tripagencymanagement.template.liquidations.application.commands;

import com.tripagencymanagement.template.liquidations.presentation.dto.UpdateIncidencyDto;

public record UpdateIncidencyCommand(
    Long liquidationId,
    Long incidencyId,
    UpdateIncidencyDto incidencyDto
) {}
