package com.tripagencymanagement.template.liquidations.application.commands;

import com.tripagencymanagement.template.liquidations.presentation.dto.UpdateFlightBookingDto;

public record UpdateFlightBookingCommand(
    Long liquidationId,
    Long flightServiceId,
    Long flightBookingId,
    UpdateFlightBookingDto flightBookingDto
) {}
