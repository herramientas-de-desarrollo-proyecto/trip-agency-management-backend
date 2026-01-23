package com.tripagencymanagement.template.liquidations.application.commands;

public record DeactivatePaymentCommand(Long liquidationId, Long paymentId) {
}
