package com.tripagencymanagement.template.users.application.commands.handlers;

import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.general.utils.exceptions.HtpExceptionUtils;
import com.tripagencymanagement.template.users.application.commands.CreateStaffCommand;
import com.tripagencymanagement.template.users.application.events.StaffCreatedDomainEvent;
import com.tripagencymanagement.template.users.domain.entities.DStaff;
import com.tripagencymanagement.template.users.domain.entities.DUser;
import com.tripagencymanagement.template.users.domain.enums.DCurrency;
import com.tripagencymanagement.template.users.domain.enums.DRoles;
import com.tripagencymanagement.template.users.domain.repositories.IStaffRepository;
import com.tripagencymanagement.template.users.domain.repositories.IUserRepository;

import jakarta.transaction.Transactional;

@Service
public class CreateStaffCommandHandler {
    private final IStaffRepository staffRepository;
    private final IUserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CreateStaffCommandHandler(IStaffRepository staffRepository,
                                    IUserRepository userRepository,
                                    ApplicationEventPublisher eventPublisher) {
        this.staffRepository = staffRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public DStaff execute(CreateStaffCommand command) {
        try {
            // Validate that user exists
            DUser user = userRepository.findById(command.staffDto().getUserId());
            if (user == null) {
                throw new IllegalArgumentException("No existe un usuario con el ID: " + command.staffDto().getUserId());
            }

            // Create domain entity
            DStaff newStaff = new DStaff();
            newStaff.setPhoneNumber(command.staffDto().getPhoneNumber());
            newStaff.setSalary(command.staffDto().getSalary());
            newStaff.setCurrency(DCurrency.valueOf(command.staffDto().getCurrency()));
            newStaff.setRole(DRoles.valueOf(command.staffDto().getRole()));
            newStaff.setHireDate(command.staffDto().getHireDate() != null ? 
                Optional.of(command.staffDto().getHireDate()) : Optional.empty());
            newStaff.setUserId(command.staffDto().getUserId());
            newStaff.setUser(user);

            // Save staff
            DStaff savedStaff = staffRepository.save(newStaff);

            // Publish domain event
            eventPublisher.publishEvent(
                new StaffCreatedDomainEvent(savedStaff.getId(), "Se creó un nuevo staff")
            );

            return savedStaff;
        } catch (Exception e) {
            throw HtpExceptionUtils.processHttpException(e);
        }
    }
}
