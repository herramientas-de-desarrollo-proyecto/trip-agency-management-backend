package com.tripagencymanagement.template.general.presentation.dtos;

import org.springframework.data.annotation.TypeAlias;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@TypeAlias("PaginatedRequestDto")
@Getter
@Setter
public class PaginatedRequestDto {
    @Nullable
    @Min(value = 1, message = "El nro de página debe ser al menos 1")
    private Integer page = 1;

    @Nullable
    @Min(value = 1, message = "El tamaño de la página debe ser al menos 1")
    private Integer size = 10;

    @Builder
    public PaginatedRequestDto(@Nullable Integer page, @Nullable Integer size) {
        this.page = (page != null && page > 0) ? page - 1 : 0;
        this.size = (size != null && size > 0) ? size : 10;
    }

    public void normalizePageNumber(){
        if (this.page != null && this.page > 0) {
            this.page = this.page - 1;
        } else {
            this.page = 0;
        }
    }
}
