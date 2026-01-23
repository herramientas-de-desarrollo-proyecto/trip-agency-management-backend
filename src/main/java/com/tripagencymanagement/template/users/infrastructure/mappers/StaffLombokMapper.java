package com.tripagencymanagement.template.users.infrastructure.mappers;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.tripagencymanagement.template.users.domain.entities.DStaff;
import com.tripagencymanagement.template.users.domain.enums.DCurrency;
import com.tripagencymanagement.template.users.domain.enums.DRoles;
import com.tripagencymanagement.template.users.infrastructure.entities.Staff;
import com.tripagencymanagement.template.users.infrastructure.enums.Currency;
import com.tripagencymanagement.template.users.infrastructure.enums.Roles;

@Component
public class StaffLombokMapper implements IStaffLombokMapper {

    private final UserLombokMapper userMapper;

    public StaffLombokMapper(UserLombokMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public Staff toPersistence(DStaff domainStaff) {
        if (domainStaff == null) {
            return null;
        }

        Staff.StaffBuilder<?, ?> staffBuilder = Staff.builder();
        staffBuilder.phoneNumber(domainStaff.getPhoneNumber());
        staffBuilder.salary(domainStaff.getSalary());
        staffBuilder.currency(domainStaff.getCurrency() != null ? Currency.valueOf(domainStaff.getCurrency().name()) : null);
        staffBuilder.hireDate(domainStaff.getHireDate() != null ? domainStaff.getHireDate().orElse(null) : null);
        staffBuilder.role(domainStaff.getRole() != null ? Roles.valueOf(domainStaff.getRole().name()) : null);

        if (domainStaff.getUser() != null) {
            staffBuilder.user(userMapper.toPersistence(domainStaff.getUser()));
        }

        if (domainStaff.getId() != null) {
            staffBuilder.id(domainStaff.getId());
        }
        if (domainStaff.getIsActive() != null) {
            staffBuilder.isActive(domainStaff.getIsActive());
        }
        if (domainStaff.getCreatedDate() != null) {
            staffBuilder.createdDate(domainStaff.getCreatedDate());
        }
        if (domainStaff.getUpdatedDate() != null) {
            staffBuilder.updatedDate(domainStaff.getUpdatedDate());
        }

        return staffBuilder.build();
    }

    @Override
    public DStaff toDomain(Staff persistenceStaff) {
        if (persistenceStaff == null) {
            return null;
        }

        DStaff domainStaff = new DStaff();
        domainStaff.setId(persistenceStaff.getId());
        domainStaff.setPhoneNumber(persistenceStaff.getPhoneNumber());
        domainStaff.setSalary(persistenceStaff.getSalary());
        domainStaff.setCurrency(persistenceStaff.getCurrency() != null ? DCurrency.valueOf(persistenceStaff.getCurrency().name()) : null);
        domainStaff.setHireDate(Optional.ofNullable(persistenceStaff.getHireDate()));
        domainStaff.setRole(persistenceStaff.getRole() != null ? DRoles.valueOf(persistenceStaff.getRole().name()) : null);

        if (persistenceStaff.getUser() != null) {
            domainStaff.setUser(userMapper.toDomain(persistenceStaff.getUser()));
            domainStaff.setUserId(persistenceStaff.getUser().getId());
        }

        domainStaff.setIsActive(persistenceStaff.getIsActive());
        domainStaff.setCreatedDate(persistenceStaff.getCreatedDate());
        domainStaff.setUpdatedDate(persistenceStaff.getUpdatedDate());

        return domainStaff;
    }
}
