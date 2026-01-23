package com.tripagencymanagement.template.users.infrastructure.mappers;

import java.time.LocalDateTime;
import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tripagencymanagement.template.users.domain.entities.DStaff;
import com.tripagencymanagement.template.users.domain.enums.DCurrency;
import com.tripagencymanagement.template.users.domain.enums.DRoles;
import com.tripagencymanagement.template.users.infrastructure.entities.Staff;
import com.tripagencymanagement.template.users.infrastructure.enums.Currency;
import com.tripagencymanagement.template.users.infrastructure.enums.Roles;

@Mapper(uses = {IUserLombokMapper.class})
public interface IStaffLombokMapper {
    Staff toPersistence(DStaff domainStaff);

    @Mapping(target = "userId", source = "user.id")
    DStaff toDomain(Staff persistenceStaff);

    // Helper methods for Optional<LocalDateTime> conversions
    default LocalDateTime mapOptionalDateTimeToDateTime(Optional<LocalDateTime> value) {
        return value != null && value.isPresent() ? value.get() : null;
    }

    default Optional<LocalDateTime> mapDateTimeToOptionalDateTime(LocalDateTime value) {
        return Optional.ofNullable(value);
    }

    // Enum mappings for Roles
    default Roles mapDRolesToRoles(DRoles role) {
        return role != null ? Roles.valueOf(role.name()) : null;
    }

    default DRoles mapRolesToDRoles(Roles role) {
        return role != null ? DRoles.valueOf(role.name()) : null;
    }

    // Enum mappings for Currency
    default Currency mapDCurrencyToCurrency(DCurrency currency) {
        return currency != null ? Currency.valueOf(currency.name()) : null;
    }

    default DCurrency mapCurrencyToDCurrency(Currency currency) {
        return currency != null ? DCurrency.valueOf(currency.name()) : null;
    }
}
