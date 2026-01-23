package com.tripagencymanagement.template.users.application.commands.handlers;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.general.utils.exceptions.HtpExceptionUtils;
import com.tripagencymanagement.template.users.application.commands.UpdateStaffCommand;
import com.tripagencymanagement.template.users.domain.entities.DStaff;
import com.tripagencymanagement.template.users.domain.enums.DCurrency;
import com.tripagencymanagement.template.users.domain.enums.DRoles;
import com.tripagencymanagement.template.users.domain.repositories.IStaffRepository;
import com.tripagencymanagement.template.users.presentation.dto.UpdateStaffDto;

import jakarta.transaction.Transactional;

@Service
public class UpdateStaffCommandHandler {
    private final IStaffRepository staffRepository;

    public UpdateStaffCommandHandler(IStaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Transactional
    public DStaff execute(UpdateStaffCommand command) {
        try {
            DStaff existingStaff = staffRepository.findById(command.staffId());
            if (existingStaff == null) {
                throw new IllegalArgumentException("No existe un staff con el ID: " + command.staffId());
            }

            UpdateStaffDto dto = command.dto();

            if (dto.getPhoneNumber() != null) {
                existingStaff.setPhoneNumber(dto.getPhoneNumber());
            }

            if (dto.getSalary() != null) {
                existingStaff.setSalary(dto.getSalary());
            }

            if (dto.getCurrency() != null) {
                existingStaff.setCurrency(DCurrency.valueOf(dto.getCurrency()));
            }

            if (dto.getRole() != null) {
                existingStaff.setRole(DRoles.valueOf(dto.getRole()));
            }

            if (dto.getHireDate() != null) {
                existingStaff.setHireDate(Optional.of(dto.getHireDate()));
            }

            return staffRepository.update(existingStaff);
        } catch (Exception e) {
            throw HtpExceptionUtils.processHttpException(e);
        }
    }
}
