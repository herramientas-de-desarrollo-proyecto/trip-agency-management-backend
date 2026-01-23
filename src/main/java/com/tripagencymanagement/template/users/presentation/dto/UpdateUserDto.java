package com.tripagencymanagement.template.users.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Valid
public class UpdateUserDto {
    @Email(message = "El correo no tiene un formato válido")
    @Size(max = 255, message = "El correo no puede exceder 255 caracteres")
    private String email;

    @Size(max = 100, message = "El nombre de usuario no puede exceder 100 caracteres")
    private String userName;

    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;
}
