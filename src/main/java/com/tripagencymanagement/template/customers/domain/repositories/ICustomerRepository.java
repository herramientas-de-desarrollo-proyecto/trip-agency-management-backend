package com.tripagency.ptc.ptcagencydemo.customers.domain.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tripagency.ptc.ptcagencydemo.customers.domain.entities.DCustomer;

public interface ICustomerRepository {
    DCustomer save(DCustomer customer);
    DCustomer update(DCustomer customer);
    DCustomer findById(Long id);
    Page<DCustomer> findAll(Pageable pageConfig);
    List<DCustomer> findAll();
    DCustomer deleteById(Long id);
    boolean existsByEmail(String email);
}
