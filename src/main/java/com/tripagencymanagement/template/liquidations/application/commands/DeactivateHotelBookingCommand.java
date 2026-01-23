package com.tripagencymanagement.template.liquidations.application.commands;

public record DeactivateHotelBookingCommand(Long liquidationId, Long hotelServiceId, Long hotelBookingId) {
}
