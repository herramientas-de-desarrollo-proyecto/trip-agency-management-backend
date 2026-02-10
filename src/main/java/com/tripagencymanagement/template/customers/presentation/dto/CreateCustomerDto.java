package com.tripagency.ptc.ptcagencydemo.customers.presentation.dto;

import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Valid
public class CreateCustomerDto {
    @NotBlank(message = "El nombre es obligatorio")
    @jakarta.validation.constraints.Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    private String lastName;

    private Optional<@Email(message = "El correo no tiene un formato válido") String> email;

    private Optional<@Pattern(regexp = "^[+]?[(]?[0-9]{1,4}[)]?[-\\s0-9]*$", message = "El teléfono no tiene un formato válido") String> phoneNumber;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
    private java.time.LocalDate birthDate;

    @NotNull(message = "El tipo de documento es obligatorio")
    private com.tripagency.ptc.ptcagencydemo.customers.domain.enums.DIdDocumentType idDocumentType;

    @NotBlank(message = "El número de documento es obligatorio")
    @Size(max = 50, message = "El número de documento no puede exceder 50 caracteres")
    private String idDocumentNumber;

    private Optional<@Size(max = 200, message = "La dirección no puede exceder 200 caracteres") String> address;

    private Optional<@Size(max = 100, message = "La nacionalidad no puede exceder 100 caracteres") String> nationality;
}
