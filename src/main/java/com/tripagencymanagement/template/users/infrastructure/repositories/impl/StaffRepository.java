package com.tripagencymanagement.template.users.infrastructure.repositories.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.tripagencymanagement.template.users.domain.entities.DStaff;
import com.tripagencymanagement.template.users.domain.enums.DRoles;
import com.tripagencymanagement.template.users.domain.repositories.IStaffRepository;
import com.tripagencymanagement.template.users.infrastructure.entities.Staff;
import com.tripagencymanagement.template.users.infrastructure.enums.Roles;
import com.tripagencymanagement.template.users.infrastructure.mappers.StaffLombokMapper;
import com.tripagencymanagement.template.users.infrastructure.repositories.interfaces.IStaffJpaRepository;

@Repository
public class StaffRepository implements IStaffRepository {
    private final IStaffJpaRepository jpaRepository;
    private final StaffLombokMapper mapper;

    public StaffRepository(IStaffJpaRepository jpaRepository, StaffLombokMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public DStaff save(DStaff staff) {
        Staff rStaff = this.mapper.toPersistence(staff);
        rStaff = jpaRepository.save(rStaff);
        return this.mapper.toDomain(rStaff);
    }

    @Override
    public DStaff update(DStaff staff) {
        Staff rStaff = this.mapper.toPersistence(staff);
        rStaff = jpaRepository.save(rStaff);
        return this.mapper.toDomain(rStaff);
    }

    @Override
    public DStaff findById(Long id) {
        return jpaRepository.findByIdAndIsActiveTrue(id).map(this.mapper::toDomain).orElse(null);
    }

    @Override
    public DStaff findByUserId(Long userId) {
        return jpaRepository.findByUserIdAndIsActiveTrue(userId).map(this.mapper::toDomain).orElse(null);
    }

    @Override
    public List<DStaff> findByRole(DRoles role) {
        Roles infraRole = Roles.valueOf(role.name());
        List<Staff> staffList = jpaRepository.findByRoleAndIsActiveTrue(infraRole);
        return staffList.stream()
                .map(this.mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DStaff> findAll(Pageable pageConfig) {
        Page<Staff> dbStaff = jpaRepository.findByIsActiveTrue(pageConfig);
        return dbStaff.map(this.mapper::toDomain);
    }

    @Override
    public DStaff deleteById(Long id) {
        DStaff staff = findById(id);
        jpaRepository.deleteById(id);
        return staff;
    }
}
