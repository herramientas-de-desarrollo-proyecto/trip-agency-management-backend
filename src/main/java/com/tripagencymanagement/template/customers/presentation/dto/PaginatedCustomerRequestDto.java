package com.tripagency.ptc.ptcagencydemo.customers.presentation.dto;

import org.springframework.data.annotation.TypeAlias;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tripagency.ptc.ptcagencydemo.general.presentation.dtos.PaginatedRequestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@TypeAlias("PaginatedCustomerRequestDto")
@Getter
@Setter
public class PaginatedCustomerRequestDto extends PaginatedRequestDto {
    public PaginatedCustomerRequestDto(int page, int size) {
        super(page, size);
    }
}
