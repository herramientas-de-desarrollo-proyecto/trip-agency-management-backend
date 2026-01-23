package com.tripagencymanagement.template.liquidations.application.commands;

import com.tripagencymanagement.template.liquidations.domain.enums.DCurrency;
import com.tripagencymanagement.template.liquidations.domain.enums.DPaymentMethod;

public record AddPaymentCommand(
    Long liquidationId,
    DPaymentMethod paymentMethod,
    float amount,
    DCurrency currency,
    String evidenceUrl
) {}
