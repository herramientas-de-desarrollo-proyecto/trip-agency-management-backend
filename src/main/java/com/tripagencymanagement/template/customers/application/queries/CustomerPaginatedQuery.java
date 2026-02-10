package com.tripagency.ptc.ptcagencydemo.customers.application.queries;

import org.jmolecules.architecture.cqrs.QueryModel;

import com.tripagency.ptc.ptcagencydemo.customers.presentation.dto.PaginatedCustomerRequestDto;

@QueryModel
public record CustomerPaginatedQuery (PaginatedCustomerRequestDto paginationConfig ){
}
