package com.tripagencymanagement.template.users.application.commands.handlers;

import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.general.utils.exceptions.HtpExceptionUtils;
import com.tripagencymanagement.template.users.application.commands.CreateUserCommand;
import com.tripagencymanagement.template.users.application.events.UserCreatedDomainEvent;
import com.tripagencymanagement.template.users.domain.entities.DUser;
import com.tripagencymanagement.template.users.domain.repositories.IUserRepository;

import jakarta.transaction.Transactional;

@Service
public class CreateUserCommandHandler {
    private final IUserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final BCryptPasswordEncoder passwordEncoder;

    public CreateUserCommandHandler(IUserRepository userRepository,
                                   ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public DUser execute(CreateUserCommand command) {
        try {
            // Validate email uniqueness
            if (userRepository.existsByEmail(command.userDto().getEmail())) {
                throw new IllegalArgumentException("Ya existe un usuario con el email: " + command.userDto().getEmail());
            }

            // Create domain entity
            DUser newUser = new DUser();
            newUser.setEmail(command.userDto().getEmail());
            newUser.setUserName(command.userDto().getUserName() != null ? 
                Optional.of(command.userDto().getUserName()) : Optional.empty());
            
            // Validate and hash password
            newUser.validatePasswordStrength(command.userDto().getPassword());
            String hashedPassword = passwordEncoder.encode(command.userDto().getPassword());
            newUser.setPasswordHash(hashedPassword);

            // Validate email format
            newUser.validateEmail();

            // Save user
            DUser savedUser = userRepository.save(newUser);

            // Publish domain event
            eventPublisher.publishEvent(
                new UserCreatedDomainEvent(savedUser.getId(), "Se creó un nuevo usuario")
            );

            return savedUser;
        } catch (Exception e) {
            throw HtpExceptionUtils.processHttpException(e);
        }
    }
}
