package com.tripagencymanagement.template.liquidations.infrastructure.mappers;

import com.tripagencymanagement.template.liquidations.domain.entities.DHotelBooking;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.HotelBooking;
import com.tripagencymanagement.template.users.infrastructure.mappers.ICurrencyMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", uses = {ILiquidationEnumMapper.class, ICurrencyMapper.class})
public interface IHotelBookingMapper {
    
    @Mapping(target = "hotelService", ignore = true)
    @Mapping(source = "roomDescription", target = "roomDescription")
    @Mapping(target = "isActive", defaultValue = "true")
    HotelBooking toInfrastructure(DHotelBooking domain);
    
    @Mapping(source = "roomDescription", target = "roomDescription")
    DHotelBooking toDomain(HotelBooking infrastructure);
    
    List<HotelBooking> toInfrastructureList(List<DHotelBooking> domainList);
    List<DHotelBooking> toDomainList(List<HotelBooking> infrastructureList);
    
    default String mapOptionalToString(Optional<String> optional) {
        return optional != null ? optional.orElse(null) : null;
    }
    
    default Optional<String> mapStringToOptional(String value) {
        return Optional.ofNullable(value);
    }
}
