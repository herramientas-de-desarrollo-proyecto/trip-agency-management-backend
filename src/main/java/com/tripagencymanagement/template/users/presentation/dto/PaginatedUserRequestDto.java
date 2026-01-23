package com.tripagencymanagement.template.users.presentation.dto;

import org.springframework.data.annotation.TypeAlias;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tripagencymanagement.template.general.presentation.dtos.PaginatedRequestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@TypeAlias("PaginatedUserRequestDto")
@Getter
@Setter
public class PaginatedUserRequestDto extends PaginatedRequestDto {
    public PaginatedUserRequestDto(int page, int size) {
        super(page, size);
    }
}
