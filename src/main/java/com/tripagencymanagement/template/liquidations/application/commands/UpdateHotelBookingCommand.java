package com.tripagencymanagement.template.liquidations.application.commands;

import com.tripagencymanagement.template.liquidations.presentation.dto.UpdateHotelBookingDto;

public record UpdateHotelBookingCommand(
    Long liquidationId,
    Long hotelServiceId,
    Long hotelBookingId,
    UpdateHotelBookingDto hotelBookingDto
) {}
