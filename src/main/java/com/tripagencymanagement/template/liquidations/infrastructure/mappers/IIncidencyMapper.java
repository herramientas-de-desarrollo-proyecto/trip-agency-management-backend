package com.tripagencymanagement.template.liquidations.infrastructure.mappers;

import com.tripagencymanagement.template.liquidations.domain.entities.DIncidency;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.Incidency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", uses = { ILiquidationEnumMapper.class })
public interface IIncidencyMapper {

    @Mapping(target = "liquidation", ignore = true)
    @Mapping(source = "amount", target = "amount")
    @Mapping(target = "isActive", defaultValue = "true")
    Incidency toInfrastructure(DIncidency domain);

    @Mapping(source = "amount", target = "amount")
    DIncidency toDomain(Incidency infrastructure);

    List<Incidency> toInfrastructureList(List<DIncidency> domainList);

    List<DIncidency> toDomainList(List<Incidency> infrastructureList);

    default Float mapOptionalToFloat(Optional<Float> optional) {
        return optional != null ? optional.orElse(null) : null;
    }

    default Optional<Float> mapFloatToOptional(Float value) {
        return Optional.ofNullable(value);
    }
}
