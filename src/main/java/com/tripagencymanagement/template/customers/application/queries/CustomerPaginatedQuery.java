package com.tripagencymanagement.template.customers.application.queries;

import org.jmolecules.architecture.cqrs.QueryModel;

import com.tripagencymanagement.template.customers.presentation.dto.PaginatedCustomerRequestDto;

@QueryModel
public record CustomerPaginatedQuery (PaginatedCustomerRequestDto paginationConfig ){
}
