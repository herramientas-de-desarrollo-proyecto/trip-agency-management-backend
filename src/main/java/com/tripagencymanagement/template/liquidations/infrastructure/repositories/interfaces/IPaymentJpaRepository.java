package com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tripagencymanagement.template.liquidations.infrastructure.entities.Payment;

@Repository
public interface IPaymentJpaRepository extends JpaRepository<Payment, Long> {
    
    List<Payment> findByLiquidationId(Long liquidationId);
}
