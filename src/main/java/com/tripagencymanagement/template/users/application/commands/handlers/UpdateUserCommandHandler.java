package com.tripagencymanagement.template.users.application.commands.handlers;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.general.utils.exceptions.HtpExceptionUtils;
import com.tripagencymanagement.template.users.application.commands.UpdateUserCommand;
import com.tripagencymanagement.template.users.domain.entities.DUser;
import com.tripagencymanagement.template.users.domain.repositories.IUserRepository;
import com.tripagencymanagement.template.users.presentation.dto.UpdateUserDto;

import jakarta.transaction.Transactional;

@Service
public class UpdateUserCommandHandler {
    private final IUserRepository userRepository;

    public UpdateUserCommandHandler(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public DUser execute(UpdateUserCommand command) {
        try {
            DUser existingUser = userRepository.findById(command.userId());
            if (existingUser == null) {
                throw new IllegalArgumentException("No existe un usuario con el ID: " + command.userId());
            }

            UpdateUserDto dto = command.dto();

            if (dto.getEmail() != null) {
                // Check if email is already taken by another user
                DUser userWithEmail = userRepository.findByEmail(dto.getEmail());
                if (userWithEmail != null && !userWithEmail.getId().equals(command.userId())) {
                    throw new IllegalArgumentException("El email ya está en uso por otro usuario");
                }
                existingUser.setEmail(dto.getEmail());
            }

            if (dto.getUserName() != null) {
                existingUser.setUserName(Optional.of(dto.getUserName()));
            }

            if (dto.getPassword() != null) {
                existingUser.setPasswordHash(dto.getPassword());
            }

            return userRepository.update(existingUser);
        } catch (Exception e) {
            throw HtpExceptionUtils.processHttpException(e);
        }
    }
}
