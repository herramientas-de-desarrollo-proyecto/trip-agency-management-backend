package com.tripagencymanagement.template.users.application.queries.handlers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.users.application.queries.UserPaginatedQuery;
import com.tripagencymanagement.template.users.domain.entities.DUser;
import com.tripagencymanagement.template.users.domain.repositories.IUserRepository;

@Service
public class UserPaginatedQueryHandler {
    private final IUserRepository userRepository;

    public UserPaginatedQueryHandler(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<DUser> execute(UserPaginatedQuery query) {
        Pageable pageable = PageRequest.of(
            query.requestDto().getPage(),
            query.requestDto().getSize()
        );
        return userRepository.findAll(pageable);
    }
}
