package com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tripagencymanagement.template.liquidations.infrastructure.entities.Incidency;

@Repository
public interface IIncidencyJpaRepository extends JpaRepository<Incidency, Long> {
    
    List<Incidency> findByLiquidationId(Long liquidationId);
}
