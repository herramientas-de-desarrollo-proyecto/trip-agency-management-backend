package com.tripagencymanagement.template.liquidations.application.queries;

import com.tripagencymanagement.template.liquidations.domain.enums.DLiquidationStatus;
import com.tripagencymanagement.template.liquidations.presentation.dto.PaginatedLiquidationRequestDto;

public record GetLiquidationsByStatusQuery(DLiquidationStatus status, PaginatedLiquidationRequestDto requestDto) {
}
