package com.tripagencymanagement.template.liquidations.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tripagencymanagement.template.liquidations.domain.entities.DHotelService;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.HotelService;
import com.tripagencymanagement.template.users.infrastructure.mappers.ICurrencyMapper;

@Mapper(componentModel = "spring", uses = {IHotelBookingMapper.class, ICurrencyMapper.class})
public interface IHotelServiceMapper {
    
    @Mapping(target = "liquidation", ignore = true)
    @Mapping(target = "isActive", defaultValue = "true")
    HotelService toInfrastructure(DHotelService domain);
    
    @org.mapstruct.AfterMapping
    default void setRelationships(@org.mapstruct.MappingTarget HotelService target) {
        if (target.getHotelBookings() != null) {
            target.getHotelBookings().forEach(booking -> booking.setHotelService(target));
        }
    }
    
    DHotelService toDomain(HotelService infrastructure);
    
    List<HotelService> toInfrastructureList(List<DHotelService> domainList);
    List<DHotelService> toDomainList(List<HotelService> infrastructureList);
}
