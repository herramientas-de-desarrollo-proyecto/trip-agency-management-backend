package com.tripagency.ptc.ptcagencydemo.customers.infrastructure.mappers;

import java.util.Optional;

import org.mapstruct.Mapper;

import com.tripagency.ptc.ptcagencydemo.customers.domain.entities.DCustomer;
import com.tripagency.ptc.ptcagencydemo.customers.infrastructure.entities.Customer;

@Mapper
public interface ICustomerLombokMapper {
    Customer toPersistence(DCustomer domainCustomer);

    DCustomer toDomain(Customer persistenceCustomer);

    // MapStruct will pick these helper methods to convert Optional<String> <->
    // String
    default String map(Optional<String> value) {
        return value != null && value.isPresent() ? value.get() : null;
    }

    default Optional<String> map(String value) {
        return Optional.ofNullable(value);
    }
}
