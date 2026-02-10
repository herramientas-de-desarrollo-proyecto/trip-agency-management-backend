package com.tripagency.ptc.ptcagencydemo.customers.infrastructure.repositories.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.tripagency.ptc.ptcagencydemo.customers.domain.entities.DCustomer;
import com.tripagency.ptc.ptcagencydemo.customers.domain.repositories.ICustomerRepository;
import com.tripagency.ptc.ptcagencydemo.customers.infrastructure.entities.Customer;
import com.tripagency.ptc.ptcagencydemo.customers.infrastructure.mappers.CustomerLombokMapper;
import com.tripagency.ptc.ptcagencydemo.customers.infrastructure.repositories.interfaces.ICustomerJpaRepository;

@Repository
public class CustomerRepository implements ICustomerRepository {
    private final ICustomerJpaRepository jpaRepository;
    private final CustomerLombokMapper mapper;

    public CustomerRepository(ICustomerJpaRepository jpaRepository, CustomerLombokMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public DCustomer save(DCustomer customer) {
        Customer rCustomer = this.mapper.toPersistence(customer);
        rCustomer = jpaRepository.save(rCustomer);
        return this.mapper.toDomain(rCustomer);
    }

    @Override
    public DCustomer update(DCustomer customer) {
        Customer rCustomer = this.mapper.toPersistence(customer);
        rCustomer = jpaRepository.save(rCustomer);
        return this.mapper.toDomain(rCustomer);
    }

    @Override
    public DCustomer findById(Long id) {
        return jpaRepository.findByIdAndIsActiveTrue(id).map(this.mapper::toDomain).orElse(null);
    }

    @Override
    public java.util.List<DCustomer> findAll() {
        return jpaRepository.findByIsActiveTrue().stream()
                .map(this.mapper::toDomain)
                .toList();
    }

    @Override
    public Page<DCustomer> findAll(Pageable pageConfig) {
        Page<Customer> dbCustomers = jpaRepository.findByIsActiveTrue(pageConfig);
        return dbCustomers.map(this.mapper::toDomain);
    }

    @Override
    public DCustomer deleteById(Long id) {
        DCustomer customer = findById(id);
        jpaRepository.deleteById(id);
        return customer;
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}
