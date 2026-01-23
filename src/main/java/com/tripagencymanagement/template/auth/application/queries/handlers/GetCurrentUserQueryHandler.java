package com.tripagencymanagement.template.auth.application.queries.handlers;

import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.auth.application.queries.GetCurrentUserQuery;
import com.tripagencymanagement.template.auth.presentation.dto.AuthResponseDto;
import com.tripagencymanagement.template.users.domain.entities.DUser;
import com.tripagencymanagement.template.users.domain.repositories.IUserRepository;
import com.tripagencymanagement.template.general.utils.exceptions.HtpExceptionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler for getting current user query.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GetCurrentUserQueryHandler {

    private final IUserRepository userRepository;

    public AuthResponseDto.UserInfoDto execute(GetCurrentUserQuery query) {
        log.debug("Getting current user info for user ID: {}", query.userId());

        DUser user = userRepository.findById(query.userId());

        if (user == null) {
            throw HtpExceptionUtils.processHttpException(
                    new IllegalArgumentException("Usuario no encontrado")
            );
        }

        return AuthResponseDto.UserInfoDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userName(user.getUserName().orElse(null))
                .isActive(user.getIsActive())
                .build();
    }
}
