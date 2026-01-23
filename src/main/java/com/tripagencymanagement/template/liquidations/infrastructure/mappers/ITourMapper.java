package com.tripagencymanagement.template.liquidations.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tripagencymanagement.template.liquidations.domain.entities.DTour;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.Tour;
import com.tripagencymanagement.template.users.infrastructure.mappers.ICurrencyMapper;

@Mapper(componentModel = "spring", uses = {ILiquidationEnumMapper.class, ICurrencyMapper.class})
public interface ITourMapper {
    
    @Mapping(target = "tourService", ignore = true)
    @Mapping(target = "isActive", defaultValue = "true")
    Tour toInfrastructure(DTour domain);
    
    DTour toDomain(Tour infrastructure);
    
    List<Tour> toInfrastructureList(List<DTour> domainList);
    List<DTour> toDomainList(List<Tour> infrastructureList);
}
