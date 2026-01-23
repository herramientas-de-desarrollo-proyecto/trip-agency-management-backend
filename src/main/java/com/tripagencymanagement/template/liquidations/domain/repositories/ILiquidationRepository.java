package com.tripagencymanagement.template.liquidations.domain.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tripagencymanagement.template.liquidations.domain.entities.DLiquidation;
import com.tripagencymanagement.template.liquidations.domain.enums.DLiquidationStatus;

public interface ILiquidationRepository {

    DLiquidation save(DLiquidation liquidation);

    Optional<DLiquidation> findById(Long id);

    Page<DLiquidation> findAll(Pageable pageable);

    Page<DLiquidation> findByCustomerId(Long customerId, Pageable pageable);

    Page<DLiquidation> findByStatus(DLiquidationStatus status, Pageable pageable);

    void deleteById(Long id);

    boolean existsById(Long id);
}
