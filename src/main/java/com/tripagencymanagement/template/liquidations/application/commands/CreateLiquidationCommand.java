package com.tripagencymanagement.template.liquidations.application.commands;

import java.time.LocalDateTime;

public record CreateLiquidationCommand(
    float currencyRate,
    LocalDateTime paymentDeadline,
    int companion,
    Long customerId,
    Long staffId
) {}
