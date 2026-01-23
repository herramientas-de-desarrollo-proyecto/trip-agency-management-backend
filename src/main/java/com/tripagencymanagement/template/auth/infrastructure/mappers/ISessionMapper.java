package com.tripagencymanagement.template.auth.infrastructure.mappers;

import java.util.Optional;

import com.tripagencymanagement.template.auth.domain.entities.DSession;
import com.tripagencymanagement.template.auth.infrastructure.entities.Session;

/**
 * Mapper interface for Session entity conversions.
 */
public interface ISessionMapper {
    
    Session toPersistence(DSession domainSession);
    
    DSession toDomain(Session persistenceSession);
    
    // Helper methods for Optional<String> conversions
    default String mapOptionalStringToString(Optional<String> value) {
        return value != null && value.isPresent() ? value.get() : null;
    }

    default Optional<String> mapStringToOptionalString(String value) {
        return Optional.ofNullable(value);
    }
}
