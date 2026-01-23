package com.tripagencymanagement.template.users.infrastructure.mappers;

import com.tripagencymanagement.template.users.domain.enums.DCurrency;
import com.tripagencymanagement.template.users.infrastructure.enums.Currency;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICurrencyMapper {
    
    Currency toInfrastructure(DCurrency domain);
    DCurrency toDomain(Currency infrastructure);
}
