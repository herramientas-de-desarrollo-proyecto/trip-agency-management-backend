package com.tripagencymanagement.template.liquidations.application.commands;

import com.tripagencymanagement.template.liquidations.presentation.dto.AddTourServiceDto;

public record AddTourServiceCommand(
    Long liquidationId,
    AddTourServiceDto tourServiceDto
) {}
