package com.tripagencymanagement.template.users.presentation.dto;

import java.time.LocalDateTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Valid
public class CreateStaffDto {
    @NotNull(message = "El ID de usuario es obligatorio")
    private Long userId;

    @NotBlank(message = "El número de teléfono es obligatorio")
    @Pattern(regexp = "^[+]?[(]?[0-9]{1,4}[)]?[-\\s0-9]*$", message = "El teléfono no tiene un formato válido")
    private String phoneNumber;

    @NotNull(message = "El salario es obligatorio")
    @Positive(message = "El salario debe ser un valor positivo")
    private Float salary;

    @NotBlank(message = "La moneda es obligatoria")
    @Pattern(regexp = "PEN|USD", message = "La moneda debe ser PEN o USD")
    private String currency;

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "SALES|COUNTER|ACCOUNTING|OPERATIONS|SUPERADMIN|SUPPORT", message = "El rol no es válido")
    private String role;

    private LocalDateTime hireDate;
}
