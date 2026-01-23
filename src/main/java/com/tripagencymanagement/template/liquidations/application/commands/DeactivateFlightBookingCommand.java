package com.tripagencymanagement.template.liquidations.application.commands;

public record DeactivateFlightBookingCommand(Long liquidationId, Long flightServiceId, Long flightBookingId) {
}
