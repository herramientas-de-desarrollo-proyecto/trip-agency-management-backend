package com.tripagencymanagement.template.liquidations.application.queries;

import com.tripagencymanagement.template.liquidations.presentation.dto.PaginatedLiquidationRequestDto;

public record GetLiquidationsByCustomerQuery(Long customerId, PaginatedLiquidationRequestDto requestDto) {
}
