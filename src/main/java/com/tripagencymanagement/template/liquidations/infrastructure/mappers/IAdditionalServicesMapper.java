package com.tripagencymanagement.template.liquidations.infrastructure.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tripagencymanagement.template.liquidations.domain.entities.DAdditionalServices;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.AdditionalServices;
import com.tripagencymanagement.template.users.infrastructure.mappers.ICurrencyMapper;

@Mapper(componentModel = "spring", uses = {ILiquidationEnumMapper.class, ICurrencyMapper.class})
public interface IAdditionalServicesMapper {
    
    @Mapping(target = "liquidation", ignore = true)
    @Mapping(target = "isActive", defaultValue = "true")
    AdditionalServices toInfrastructure(DAdditionalServices domain);
    
    DAdditionalServices toDomain(AdditionalServices infrastructure);
    
    List<AdditionalServices> toInfrastructureList(List<DAdditionalServices> domainList);
    List<DAdditionalServices> toDomainList(List<AdditionalServices> infrastructureList);
}
