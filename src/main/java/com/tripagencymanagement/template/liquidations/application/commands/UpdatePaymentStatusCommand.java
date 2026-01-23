package com.tripagencymanagement.template.liquidations.application.commands;

import com.tripagencymanagement.template.liquidations.domain.enums.DPaymentStatus;

public record UpdatePaymentStatusCommand(
    Long liquidationId,
    DPaymentStatus targetStatus
) {}
