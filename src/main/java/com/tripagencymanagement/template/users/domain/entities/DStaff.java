package com.tripagencymanagement.template.users.domain.entities;

import java.time.LocalDateTime;
import java.util.Optional;

import com.tripagencymanagement.template.general.entities.domainEntities.BaseAbstractDomainEntity;
import com.tripagencymanagement.template.users.domain.enums.DCurrency;
import com.tripagencymanagement.template.users.domain.enums.DRoles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DStaff extends BaseAbstractDomainEntity {
    private String phoneNumber;
    private Float salary;
    private DCurrency currency;
    private Optional<LocalDateTime> hireDate = Optional.empty();
    private DRoles role;
    private Long userId;
    private DUser user;

    /**
     * Assigns a new role to the staff member
     * @param newRole the role to assign
     * @throws IllegalArgumentException if newRole is null
     */
    public void assignRole(DRoles newRole) {
        if (newRole == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        this.role = newRole;
    }

    /**
     * Updates the salary and currency for the staff member
     * @param newSalary the new salary amount
     * @param newCurrency the currency for the salary
     * @throws IllegalArgumentException if salary is negative or currency is null
     */
    public void updateSalary(Float newSalary, DCurrency newCurrency) {
        if (newSalary == null || newSalary < 0) {
            throw new IllegalArgumentException("Salary must be a positive value");
        }
        if (newCurrency == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        }
        this.salary = newSalary;
        this.currency = newCurrency;
    }

    @Override
    public String toString() {
        return "DStaff{" +
                "id=" + getId() +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", salary=" + salary +
                ", currency=" + currency +
                ", hireDate=" + hireDate.orElse(null) +
                ", role=" + role +
                ", userId=" + userId +
                ", user=" + (user != null ? user.getEmail() : null) +
                ", isActive=" + getIsActive() +
                ", createdDate=" + getCreatedDate() +
                ", updatedDate=" + getUpdatedDate() +
                '}';
    }
}
