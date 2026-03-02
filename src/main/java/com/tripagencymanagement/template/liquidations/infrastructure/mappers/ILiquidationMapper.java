package com.tripagencymanagement.template.liquidations.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tripagencymanagement.template.customers.infrastructure.mappers.ICustomerLombokMapper;
import com.tripagencymanagement.template.liquidations.domain.entities.DLiquidation;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.Liquidation;
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
public interface ILiquidationMapper {

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "staffOnCharge", ignore = true)
    @Mapping(target = "isActive", defaultValue = "true")
    Liquidation toInfrastructure(DLiquidation domain);
    
    @org.mapstruct.AfterMapping
    default void setLiquidationRelationships(@org.mapstruct.MappingTarget Liquidation target) {
        // Set bidirectional relationships for all services
        if (target.getFlightServices() != null) {
            target.getFlightServices().forEach(service -> service.setLiquidation(target));
        }
        if (target.getHotelServices() != null) {
            target.getHotelServices().forEach(service -> service.setLiquidation(target));
        }
        if (target.getTourServices() != null) {
            target.getTourServices().forEach(service -> service.setLiquidation(target));
        }
        if (target.getAdditionalServices() != null) {
            target.getAdditionalServices().forEach(service -> service.setLiquidation(target));
        }
        if (target.getPayments() != null) {
            target.getPayments().forEach(payment -> payment.setLiquidation(target));
        }
        if (target.getIncidencies() != null) {
            target.getIncidencies().forEach(incidency -> incidency.setLiquidation(target));
        }
    }

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "staffOnCharge", ignore = true)
    DLiquidation toDomain(Liquidation infrastructure);

    @org.mapstruct.AfterMapping
    default void filterInactiveChildren(@org.mapstruct.MappingTarget DLiquidation target) {
        if (target.getPayments() != null) {
            target.setPayments(target.getPayments().stream()
                .filter(p -> Boolean.TRUE.equals(p.getIsActive()))
                .collect(java.util.stream.Collectors.toList()));
        }
        if (target.getAdditionalServices() != null) {
            target.setAdditionalServices(target.getAdditionalServices().stream()
                .filter(s -> Boolean.TRUE.equals(s.getIsActive()))
                .collect(java.util.stream.Collectors.toList()));
        }
        if (target.getIncidencies() != null) {
            target.setIncidencies(target.getIncidencies().stream()
                .filter(i -> Boolean.TRUE.equals(i.getIsActive()))
                .collect(java.util.stream.Collectors.toList()));
        }
    }

    List<Liquidation> toInfrastructureList(List<DLiquidation> domainList);

    List<DLiquidation> toDomainList(List<Liquidation> infrastructureList);
    
    @org.mapstruct.AfterMapping
    default void mapRelationships(@org.mapstruct.MappingTarget DLiquidation target, Liquidation source) {
        if (source.getCustomer() != null) {
            ICustomerLombokMapper customerMapper = org.mapstruct.factory.Mappers.getMapper(ICustomerLombokMapper.class);
            target.setCustomer(java.util.Optional.of(customerMapper.toDomain(source.getCustomer())));
        } else {
            target.setCustomer(java.util.Optional.empty());
        }
        
        if (source.getStaffOnCharge() != null) {
            IStaffLombokMapper staffMapper = org.mapstruct.factory.Mappers.getMapper(IStaffLombokMapper.class);
            target.setStaffOnCharge(java.util.Optional.of(staffMapper.toDomain(source.getStaffOnCharge())));
        } else {
            target.setStaffOnCharge(java.util.Optional.empty());
        }
    }
}
