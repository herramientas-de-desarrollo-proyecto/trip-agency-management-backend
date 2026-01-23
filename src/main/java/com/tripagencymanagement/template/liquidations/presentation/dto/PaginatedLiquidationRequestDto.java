package com.tripagencymanagement.template.liquidations.presentation.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tripagencymanagement.template.general.presentation.dtos.PaginatedRequestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
public class PaginatedLiquidationRequestDto extends PaginatedRequestDto {
    public PaginatedLiquidationRequestDto(int page, int size) {
        super(page, size);
    }
}
