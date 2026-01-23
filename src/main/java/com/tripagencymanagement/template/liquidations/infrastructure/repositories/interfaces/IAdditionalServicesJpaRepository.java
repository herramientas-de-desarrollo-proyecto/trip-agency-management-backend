package com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tripagencymanagement.template.liquidations.infrastructure.entities.AdditionalServices;

@Repository
public interface IAdditionalServicesJpaRepository extends JpaRepository<AdditionalServices, Long> {
    
    List<AdditionalServices> findByLiquidationId(Long liquidationId);
}
