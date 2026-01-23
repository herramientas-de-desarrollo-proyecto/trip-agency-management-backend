package com.tripagencymanagement.template.users.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Valid
public class CreateUserDto {
    @Email(message = "El correo no tiene un formato válido")
    @NotBlank(message = "El correo es obligatorio")
    @Size(max = 255, message = "El correo no puede exceder 255 caracteres")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @Size(max = 100, message = "El nombre de usuario no puede exceder 100 caracteres")
    private String userName;
}
