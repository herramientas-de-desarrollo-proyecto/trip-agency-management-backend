package com.tripagencymanagement.template.users.application.queries;

import com.tripagencymanagement.template.users.domain.enums.DRoles;

public record GetStaffByRoleQuery(DRoles role) {
}
