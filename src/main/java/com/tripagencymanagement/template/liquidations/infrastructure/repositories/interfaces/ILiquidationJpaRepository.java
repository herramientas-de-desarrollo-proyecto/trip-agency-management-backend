package com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tripagencymanagement.template.liquidations.infrastructure.entities.Liquidation;
import com.tripagencymanagement.template.liquidations.infrastructure.enums.LiquidationStatus;

@Repository
public interface ILiquidationJpaRepository extends JpaRepository<Liquidation, Long> {
    
    Optional<Liquidation> findByIdAndIsActiveTrue(Long id);
    
    Page<Liquidation> findByIsActiveTrue(Pageable pageable);
    
    Page<Liquidation> findByCustomerId(Long customerId, Pageable pageable);
    
    Page<Liquidation> findByCustomerIdAndIsActiveTrue(Long customerId, Pageable pageable);
    
    Page<Liquidation> findByStatus(LiquidationStatus status, Pageable pageable);
    
    Page<Liquidation> findByStatusAndIsActiveTrue(LiquidationStatus status, Pageable pageable);
}
