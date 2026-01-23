package com.tripagencymanagement.template.liquidations.application.commands;

import com.tripagencymanagement.template.liquidations.presentation.dto.AddHotelServiceDto;

public record AddHotelServiceCommand(
    Long liquidationId,
    AddHotelServiceDto hotelServiceDto
) {}
