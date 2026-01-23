package com.tripagencymanagement.template.users.domain.entities;

import java.util.Optional;
import java.util.regex.Pattern;

import com.tripagencymanagement.template.general.entities.domainEntities.BaseAbstractDomainEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DUser extends BaseAbstractDomainEntity {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final int MIN_PASSWORD_LENGTH = 8;

    private Optional<String> userName = Optional.empty();
    private String email;
    private String passwordHash;

    /**
     * Validates email format
     * @throws IllegalArgumentException if email format is invalid
     */
    public void validateEmail() {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Formato de email inválido: " + email);
        }
    }

    /**
     * Validates password strength
     * @param password the plain text password to validate
     * @throws IllegalArgumentException if password doesn't meet strength requirements
     */
    public void validatePasswordStrength(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(
                "La contraseña debe tener al menos " + MIN_PASSWORD_LENGTH + " caracteres"
            );
        }
    }

    @Override
    public String toString() {
        return "DUser{" +
                "id=" + getId() +
                ", userName=" + userName.orElse(null) +
                ", email='" + email + '\'' +
                ", isActive=" + getIsActive() +
                ", createdDate=" + getCreatedDate() +
                ", updatedDate=" + getUpdatedDate() +
                '}';
    }
}
