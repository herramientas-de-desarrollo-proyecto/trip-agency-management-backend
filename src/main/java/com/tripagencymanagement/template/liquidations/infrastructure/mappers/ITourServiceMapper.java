package com.tripagencymanagement.template.liquidations.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tripagencymanagement.template.liquidations.domain.entities.DTourService;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.TourService;
import com.tripagencymanagement.template.users.infrastructure.mappers.ICurrencyMapper;

@Mapper(componentModel = "spring", uses = {ITourMapper.class, ICurrencyMapper.class})
public interface ITourServiceMapper {
    
    @Mapping(target = "liquidation", ignore = true)
    @Mapping(target = "isActive", defaultValue = "true")
    TourService toInfrastructure(DTourService domain);
    
    @org.mapstruct.AfterMapping
    default void setRelationships(@org.mapstruct.MappingTarget TourService target) {
        if (target.getTours() != null) {
            target.getTours().forEach(tour -> tour.setTourService(target));
        }
    }
    
    DTourService toDomain(TourService infrastructure);

    @org.mapstruct.AfterMapping
    default void filterInactiveTours(@org.mapstruct.MappingTarget DTourService target) {
        if (target.getTours() != null) {
            target.setTours(target.getTours().stream()
                .filter(tour -> Boolean.TRUE.equals(tour.getIsActive()))
                .collect(java.util.stream.Collectors.toList()));
        }
    }
    
    List<TourService> toInfrastructureList(List<DTourService> domainList);
    List<DTourService> toDomainList(List<TourService> infrastructureList);
}
