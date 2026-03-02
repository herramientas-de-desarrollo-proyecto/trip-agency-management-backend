package com.tripagencymanagement.template.liquidations.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tripagencymanagement.template.liquidations.domain.entities.DFlightService;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.FlightService;
import com.tripagencymanagement.template.users.infrastructure.mappers.ICurrencyMapper;

@Mapper(componentModel = "spring", uses = {IFlightBookingMapper.class, ICurrencyMapper.class})
public interface IFlightServiceMapper {
    
    @Mapping(target = "liquidation", ignore = true)
    @Mapping(target = "isActive", defaultValue = "true")
    FlightService toInfrastructure(DFlightService domain);
    
    @org.mapstruct.AfterMapping
    default void setRelationships(@org.mapstruct.MappingTarget FlightService target) {
        if (target.getFlightBookings() != null) {
            target.getFlightBookings().forEach(booking -> booking.setFlightService(target));
        }
    }
    
    DFlightService toDomain(FlightService infrastructure);

    @org.mapstruct.AfterMapping
    default void filterInactiveFlightBookings(@org.mapstruct.MappingTarget DFlightService target) {
        if (target.getFlightBookings() != null) {
            target.setFlightBookings(target.getFlightBookings().stream()
                .filter(booking -> Boolean.TRUE.equals(booking.getIsActive()))
                .collect(java.util.stream.Collectors.toList()));
        }
    }
    
    List<FlightService> toInfrastructureList(List<DFlightService> domainList);
    List<DFlightService> toDomainList(List<FlightService> infrastructureList);
}
