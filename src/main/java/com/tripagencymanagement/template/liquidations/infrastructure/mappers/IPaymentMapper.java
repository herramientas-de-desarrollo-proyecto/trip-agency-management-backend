package com.tripagencymanagement.template.liquidations.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tripagencymanagement.template.liquidations.domain.entities.DPayment;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.Payment;

@Mapper(componentModel = "spring", uses = {ILiquidationEnumMapper.class})
public interface IPaymentMapper {
    
    @Mapping(target = "liquidation", ignore = true)
    @Mapping(target = "isActive", defaultValue = "true")
    Payment toInfrastructure(DPayment domain);
    
    DPayment toDomain(Payment infrastructure);
    
    List<Payment> toInfrastructureList(List<DPayment> domainList);
    List<DPayment> toDomainList(List<Payment> infrastructureList);
}
