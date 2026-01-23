package com.tripagencymanagement.template.liquidations.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tripagencymanagement.template.customers.infrastructure.mappers.ICustomerLombokMapper;
import com.tripagencymanagement.template.liquidations.domain.entities.DLiquidation;
import com.tripagencymanagement.template.liquidations.presentation.dto.LiquidationWithDetailsDto;
import com.tripagencymanagement.template.users.infrastructure.mappers.IStaffLombokMapper;

@Mapper(componentModel = "spring", uses = {
        ILiquidationEnumMapper.class,
        IPaymentMapper.class,
        IFlightServiceMapper.class,
        IHotelServiceMapper.class,
        ITourServiceMapper.class,
        IAdditionalServicesMapper.class,
        IIncidencyMapper.class,
        ICustomerLombokMapper.class,
        IStaffLombokMapper.class
})
public interface ILiquidationWithDetailsMapper {
    
    @Mapping(target = "customer", expression = "java(domain.getCustomer().orElse(null))")
    @Mapping(target = "staffOnCharge", expression = "java(domain.getStaffOnCharge().orElse(null))")
    @Mapping(target = "createdAt", source = "createdDate")
    @Mapping(target = "updatedAt", source = "updatedDate")
    LiquidationWithDetailsDto toDto(DLiquidation domain);
}
