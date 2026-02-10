package com.tripagency.ptc.ptcagencydemo.customers.application.queries.handlers;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tripagency.ptc.ptcagencydemo.customers.application.queries.GetAllCustomersQuery;
import com.tripagency.ptc.ptcagencydemo.customers.domain.entities.DCustomer;
import com.tripagency.ptc.ptcagencydemo.customers.domain.repositories.ICustomerRepository;
import com.tripagency.ptc.ptcagencydemo.general.utils.exceptions.HtpExceptionUtils;

@Service
public class GetAllCustomersQueryHandler {
    private final ICustomerRepository customerRepository;

    public GetAllCustomersQueryHandler(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<DCustomer> execute(GetAllCustomersQuery query) {
        try {
            return customerRepository.findAll();
        } catch (Exception e) {
            throw HtpExceptionUtils.processHttpException(e);
        }
    }
}
