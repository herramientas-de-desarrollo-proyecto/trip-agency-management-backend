package com.tripagencymanagement.template.liquidations.application.commands;

import com.tripagencymanagement.template.liquidations.presentation.dto.UpdateTourDto;

public record UpdateTourCommand(
    Long liquidationId,
    Long tourServiceId,
    Long tourId,
    UpdateTourDto tourDto
) {}
