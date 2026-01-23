package com.tripagencymanagement.template.users.application.commands.handlers;

import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.general.utils.exceptions.HtpExceptionUtils;
import com.tripagencymanagement.template.users.application.commands.DeactivateUserCommand;
import com.tripagencymanagement.template.users.domain.entities.DUser;
import com.tripagencymanagement.template.users.domain.repositories.IUserRepository;

import jakarta.transaction.Transactional;

@Service
public class DeactivateUserCommandHandler {
    private final IUserRepository userRepository;

    public DeactivateUserCommandHandler(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public DUser execute(DeactivateUserCommand command) {
        try {
            DUser existingUser = userRepository.findById(command.userId());
            if (existingUser == null) {
                throw new IllegalArgumentException("No existe un usuario con el ID: " + command.userId());
            }

            existingUser.setIsActive(false);
            return userRepository.update(existingUser);
        } catch (Exception e) {
            throw HtpExceptionUtils.processHttpException(e);
        }
    }
}
