package com.tripagencymanagement.template.users.presentation.dto;

import java.time.LocalDateTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Valid
public class UpdateStaffDto {
    @Pattern(regexp = "^[+]?[(]?[0-9]{1,4}[)]?[-\\s0-9]*$", message = "El teléfono no tiene un formato válido")
    private String phoneNumber;

    @Positive(message = "El salario debe ser un valor positivo")
    private Float salary;

    @Pattern(regexp = "PEN|USD", message = "La moneda debe ser PEN o USD")
    private String currency;

    @Pattern(regexp = "SALES|COUNTER|ACCOUNTING|OPERATIONS|SUPERADMIN|SUPPORT", message = "El rol no es válido")
    private String role;

    private LocalDateTime hireDate;
}
