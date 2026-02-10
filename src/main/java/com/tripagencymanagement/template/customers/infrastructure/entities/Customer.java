    package com.tripagency.ptc.ptcagencydemo.customers.infrastructure.entities;

import java.time.LocalDate;

import com.tripagency.ptc.ptcagencydemo.customers.infrastructure.enums.IdDocumentType;
import com.tripagency.ptc.ptcagencydemo.general.entities.repositoryEntites.BaseAbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "customers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Customer extends BaseAbstractEntity{

    @Column(nullable = false, length = 150)
    private String firstName;

    @Column(nullable = false, length = 150)
    private String lastName;

    @Column(nullable = true, length = 255, unique = true)
    private String email;

    @Column(nullable = true, length = 20, unique = true)
    private String phoneNumber;

    @Column(nullable = false, name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "id_document_type", length = 20)
    private IdDocumentType idDocumentType;

    @Column(nullable = false, name = "id_document_number", length = 50, unique = true)
    private String idDocumentNumber;

    @Column(nullable = true, length = 255)
    private String address;

    @Column(nullable = true, length = 100)
    private String nationality;
}
