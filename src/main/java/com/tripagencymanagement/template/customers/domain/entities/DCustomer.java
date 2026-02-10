package com.tripagency.ptc.ptcagencydemo.customers.domain.entities;

import java.time.LocalDate;
import java.util.Optional;

import com.tripagency.ptc.ptcagencydemo.customers.domain.enums.DIdDocumentType;
import com.tripagency.ptc.ptcagencydemo.general.entities.domainEntities.BaseAbstractDomainEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DCustomer extends BaseAbstractDomainEntity {
    private String firstName;
    private String lastName;
    private Optional<String> email = Optional.empty();
    private Optional<String> phoneNumber = Optional.empty();
    private LocalDate birthDate;
    private DIdDocumentType idDocumentType;
    private String idDocumentNumber;
    private Optional<String> address = Optional.empty();
    private Optional<String> nationality = Optional.empty();

    @Override
    public String toString() {
        return "DCustomer{" +
                "id=" + getId() +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email=" + email.orElse(null) +
                ", phoneNumber=" + phoneNumber.orElse(null) +
                ", birthDate=" + birthDate +
                ", idDocumentType=" + idDocumentType +
                ", idDocumentNumber='" + idDocumentNumber + '\'' +
                ", address=" + address.orElse(null) +
                ", nationality=" + nationality.orElse(null) +
                ", isActive=" + getIsActive() +
                '}';
    }
}
