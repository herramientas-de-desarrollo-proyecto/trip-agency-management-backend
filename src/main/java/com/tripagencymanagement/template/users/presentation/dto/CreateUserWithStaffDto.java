package com.tripagencymanagement.template.users.presentation.dto;

import java.time.LocalDateTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Valid
public class CreateUserWithStaffDto {
    // User fields
    @Email(message = "El correo no tiene un formato válido")
    @NotBlank(message = "El correo es obligatorio")
    @Size(max = 255, message = "El correo no puede exceder 255 caracteres")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @Size(max = 100, message = "El nombre de usuario no puede exceder 100 caracteres")
    private String userName;

    // Staff fields
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
