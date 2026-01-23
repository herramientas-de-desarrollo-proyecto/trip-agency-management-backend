package com.tripagencymanagement.template.users.application.commands.handlers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.general.utils.exceptions.HtpExceptionUtils;
import com.tripagencymanagement.template.users.application.commands.CreateUserWithStaffCommand;
import com.tripagencymanagement.template.users.application.events.StaffCreatedDomainEvent;
import com.tripagencymanagement.template.users.application.events.UserCreatedDomainEvent;
import com.tripagencymanagement.template.users.domain.entities.DStaff;
import com.tripagencymanagement.template.users.domain.entities.DUser;
import com.tripagencymanagement.template.users.domain.enums.DCurrency;
import com.tripagencymanagement.template.users.domain.enums.DRoles;
import com.tripagencymanagement.template.users.domain.repositories.IStaffRepository;
import com.tripagencymanagement.template.users.domain.repositories.IUserRepository;

import jakarta.transaction.Transactional;

@Service
public class CreateUserWithStaffCommandHandler {
    private final IUserRepository userRepository;
    private final IStaffRepository staffRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public CreateUserWithStaffCommandHandler(IUserRepository userRepository,
            IStaffRepository staffRepository,
            ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.staffRepository = staffRepository;
        this.eventPublisher = eventPublisher;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public DStaff execute(CreateUserWithStaffCommand command) {
        try {
            // Validate email uniqueness
            if (userRepository.existsByEmail(command.dto().getEmail())) {
                throw new IllegalArgumentException("Ya existe un usuario con el email: " + command.dto().getEmail());
            }

            // Create and save User first
            DUser newUser = new DUser();
            newUser.setEmail(command.dto().getEmail());
            newUser.setUserName(
                    command.dto().getUserName() != null ? Optional.of(command.dto().getUserName()) : Optional.empty());

            // Validate and hash password
            newUser.validatePasswordStrength(command.dto().getPassword());
            String hashedPassword = passwordEncoder.encode(command.dto().getPassword());
            newUser.setPasswordHash(hashedPassword);

            // Validate email format
            newUser.validateEmail();

            // Save user
            DUser savedUser = userRepository.save(newUser);

            // Publish user created event
            eventPublisher.publishEvent(
                    new UserCreatedDomainEvent(savedUser.getId(), "Se creó un nuevo usuario con staff"));

            // Create Staff linked to the user
            DStaff newStaff = new DStaff();
            newStaff.setPhoneNumber(command.dto().getPhoneNumber());
            newStaff.setSalary(command.dto().getSalary());
            newStaff.setCurrency(DCurrency.valueOf(command.dto().getCurrency()));
            newStaff.setRole(DRoles.valueOf(command.dto().getRole()));
            newStaff.setHireDate(
                    command.dto().getHireDate() != null ? Optional.of(command.dto().getHireDate()) : Optional.empty());
            newStaff.setUserId(savedUser.getId());
            newStaff.setUser(savedUser);

            // Save staff
            DStaff savedStaff = staffRepository.save(newStaff);

            // Publish staff created event
            eventPublisher.publishEvent(
                    new StaffCreatedDomainEvent(savedStaff.getId(), "Se creó un nuevo staff vinculado al usuario"));

            return savedStaff;
        } catch (Exception e) {
            throw HtpExceptionUtils.processHttpException(e);
        }
    }
}
