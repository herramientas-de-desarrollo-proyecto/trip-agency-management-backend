package com.tripagencymanagement.template.users.application.queries.handlers;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.users.application.queries.GetStaffByRoleQuery;
import com.tripagencymanagement.template.users.domain.entities.DStaff;
import com.tripagencymanagement.template.users.domain.repositories.IStaffRepository;

@Service
public class GetStaffByRoleQueryHandler {
    private final IStaffRepository staffRepository;

    public GetStaffByRoleQueryHandler(IStaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public List<DStaff> execute(GetStaffByRoleQuery query) {
        return staffRepository.findByRole(query.role());
    }
}
