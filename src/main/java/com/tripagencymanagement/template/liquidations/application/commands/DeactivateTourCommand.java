package com.tripagencymanagement.template.liquidations.application.commands;

public record DeactivateTourCommand(Long liquidationId, Long tourServiceId, Long tourId) {
}
