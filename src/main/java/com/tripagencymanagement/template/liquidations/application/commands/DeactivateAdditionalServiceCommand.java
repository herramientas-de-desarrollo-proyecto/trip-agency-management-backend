package com.tripagencymanagement.template.liquidations.application.commands;

public record DeactivateAdditionalServiceCommand(Long liquidationId, Long additionalServiceId) {
}
