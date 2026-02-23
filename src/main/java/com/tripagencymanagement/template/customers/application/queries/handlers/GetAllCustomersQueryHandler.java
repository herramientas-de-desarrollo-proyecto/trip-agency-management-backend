package com.tripagencymanagement.template.customers.application.queries.handlers;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.customers.application.queries.GetAllCustomersQuery;
import com.tripagencymanagement.template.customers.domain.entities.DCustomer;
import com.tripagencymanagement.template.customers.domain.repositories.ICustomerRepository;
import com.tripagencymanagement.template.general.utils.exceptions.HtpExceptionUtils;

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
