package com.tripagencymanagement.template.liquidations.application.commands;

public record DeactivateIncidencyCommand(Long liquidationId, Long incidencyId) {
}
