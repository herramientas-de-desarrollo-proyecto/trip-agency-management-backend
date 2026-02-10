package com.tripagency.ptc.ptcagencydemo.customers.presentation.dto;

import java.time.LocalDate;

import com.tripagency.ptc.ptcagencydemo.customers.domain.enums.DIdDocumentType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Valid
public class UpdateCustomerDto {
    @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
    private String firstName;

    @Size(max = 150, message = "El apellido no puede exceder 150 caracteres")
    private String lastName;

    @Email(message = "El correo no tiene un formato válido")
    private String email;

    @Pattern(regexp = "^[+]?[(]?[0-9]{1,4}[)]?[-\\s0-9]*$", message = "El teléfono no tiene un formato válido")
    private String phoneNumber;

    @Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
    private LocalDate birthDate;

    private DIdDocumentType idDocumentType;

    @Size(max = 50, message = "El número de documento no puede exceder 50 caracteres")
    private String idDocumentNumber;

    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    private String address;

    @Size(max = 100, message = "La nacionalidad no puede exceder 100 caracteres")
    private String nationality;
}
