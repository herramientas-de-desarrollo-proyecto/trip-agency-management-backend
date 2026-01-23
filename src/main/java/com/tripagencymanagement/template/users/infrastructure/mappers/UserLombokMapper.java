package com.tripagencymanagement.template.users.infrastructure.mappers;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.tripagencymanagement.template.users.domain.entities.DUser;
import com.tripagencymanagement.template.users.infrastructure.entities.User;

@Component
public class UserLombokMapper implements IUserLombokMapper {

    @Override
    public User toPersistence(DUser domainUser) {
        if (domainUser == null) {
            return null;
        }

        User.UserBuilder<?, ?> userBuilder = User.builder();
        userBuilder.userName(domainUser.getUserName() != null ? domainUser.getUserName().orElse(null) : null);
        userBuilder.email(domainUser.getEmail());
        userBuilder.passwordHash(domainUser.getPasswordHash());

        if (domainUser.getId() != null) {
            userBuilder.id(domainUser.getId());
        }
        if (domainUser.getIsActive() != null) {
            userBuilder.isActive(domainUser.getIsActive());
        }
        if (domainUser.getCreatedDate() != null) {
            userBuilder.createdDate(domainUser.getCreatedDate());
        }
        if (domainUser.getUpdatedDate() != null) {
            userBuilder.updatedDate(domainUser.getUpdatedDate());
        }

        return userBuilder.build();
    }

    @Override
    public DUser toDomain(User persistenceUser) {
        if (persistenceUser == null) {
            return null;
        }

        DUser domainUser = new DUser();
        domainUser.setId(persistenceUser.getId());
        domainUser.setUserName(Optional.ofNullable(persistenceUser.getUserName()));
        domainUser.setEmail(persistenceUser.getEmail());
        domainUser.setPasswordHash(persistenceUser.getPasswordHash());
        domainUser.setIsActive(persistenceUser.getIsActive());
        domainUser.setCreatedDate(persistenceUser.getCreatedDate());
        domainUser.setUpdatedDate(persistenceUser.getUpdatedDate());

        return domainUser;
    }
}
