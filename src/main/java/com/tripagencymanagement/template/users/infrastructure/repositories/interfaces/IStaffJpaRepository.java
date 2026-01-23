package com.tripagencymanagement.template.users.infrastructure.repositories.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tripagencymanagement.template.users.infrastructure.entities.Staff;
import com.tripagencymanagement.template.users.infrastructure.enums.Roles;

@Repository
public interface IStaffJpaRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByUserId(Long userId);
    Optional<Staff> findByUserIdAndIsActiveTrue(Long userId);
    Optional<Staff> findByIdAndIsActiveTrue(Long id);
    List<Staff> findByRole(Roles role);
    List<Staff> findByRoleAndIsActiveTrue(Roles role);
    Page<Staff> findByIsActiveTrue(Pageable pageable);
}
