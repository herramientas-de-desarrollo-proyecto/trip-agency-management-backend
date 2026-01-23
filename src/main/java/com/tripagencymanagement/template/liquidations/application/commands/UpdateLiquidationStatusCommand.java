package com.tripagencymanagement.template.liquidations.application.commands;

import com.tripagencymanagement.template.liquidations.domain.enums.DLiquidationStatus;

public record UpdateLiquidationStatusCommand(
    Long liquidationId,
    DLiquidationStatus targetStatus
) {}
