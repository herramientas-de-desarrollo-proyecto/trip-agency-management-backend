package com.tripagencymanagement.template.users.application.commands.handlers;

import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.general.utils.exceptions.HtpExceptionUtils;
import com.tripagencymanagement.template.users.application.commands.DeactivateStaffCommand;
import com.tripagencymanagement.template.users.domain.entities.DStaff;
import com.tripagencymanagement.template.users.domain.repositories.IStaffRepository;

import jakarta.transaction.Transactional;

@Service
public class DeactivateStaffCommandHandler {
    private final IStaffRepository staffRepository;

    public DeactivateStaffCommandHandler(IStaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Transactional
    public DStaff execute(DeactivateStaffCommand command) {
        try {
            DStaff existingStaff = staffRepository.findById(command.staffId());
            if (existingStaff == null) {
                throw new IllegalArgumentException("No existe un staff con el ID: " + command.staffId());
            }

            existingStaff.setIsActive(false);
            return staffRepository.update(existingStaff);
        } catch (Exception e) {
            throw HtpExceptionUtils.processHttpException(e);
        }
    }
}
