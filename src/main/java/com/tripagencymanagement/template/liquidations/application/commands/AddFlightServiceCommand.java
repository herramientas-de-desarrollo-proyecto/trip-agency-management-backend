package com.tripagencymanagement.template.liquidations.application.commands;

import com.tripagencymanagement.template.liquidations.presentation.dto.AddFlightServiceDto;

public record AddFlightServiceCommand(
    Long liquidationId,
    AddFlightServiceDto flightServiceDto
) {}
