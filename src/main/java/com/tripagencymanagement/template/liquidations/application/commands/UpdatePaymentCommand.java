package com.tripagencymanagement.template.liquidations.application.commands;

import com.tripagencymanagement.template.liquidations.presentation.dto.UpdatePaymentDto;

public record UpdatePaymentCommand(
    Long liquidationId,
    Long paymentId,
    UpdatePaymentDto paymentDto
) {}
