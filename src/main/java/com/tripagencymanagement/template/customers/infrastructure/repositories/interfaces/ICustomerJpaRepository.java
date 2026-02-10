package com.tripagency.ptc.ptcagencydemo.customers.infrastructure.repositories.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tripagency.ptc.ptcagencydemo.customers.infrastructure.entities.Customer;

public interface ICustomerJpaRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);
    Optional<Customer> findByIdAndIsActiveTrue(Long id);
    List<Customer> findByIsActiveTrue();
    Page<Customer> findByIsActiveTrue(Pageable pageable);
}
