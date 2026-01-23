package com.tripagencymanagement.template.liquidations.infrastructure.mappers;

import com.tripagencymanagement.template.liquidations.domain.enums.*;
import com.tripagencymanagement.template.liquidations.infrastructure.enums.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ILiquidationEnumMapper {
    
    // LiquidationStatus
    LiquidationStatus toInfrastructure(DLiquidationStatus domain);
    DLiquidationStatus toDomain(LiquidationStatus infrastructure);
    
    // PaymentStatus
    PaymentStatus toInfrastructure(DPaymentStatus domain);
    DPaymentStatus toDomain(PaymentStatus infrastructure);
    
    // PaymentMethod
    PaymentMethod toInfrastructure(DPaymentMethod domain);
    DPaymentMethod toDomain(PaymentMethod infrastructure);
    
    // ServiceStatus
    ServiceStatus toInfrastructure(DServiceStatus domain);
    DServiceStatus toDomain(ServiceStatus infrastructure);
    
    // IncidencyStatus
    IncidencyStatus toInfrastructure(DIncidencyStatus domain);
    DIncidencyStatus toDomain(IncidencyStatus infrastructure);
    
    // PaymentValidity
    PaymentValidity toInfrastructure(DPaymentValidity domain);
    DPaymentValidity toDomain(PaymentValidity infrastructure);
    
    // Currency
    Currency toInfrastructure(DCurrency domain);
    DCurrency toDomain(Currency infrastructure);
}
