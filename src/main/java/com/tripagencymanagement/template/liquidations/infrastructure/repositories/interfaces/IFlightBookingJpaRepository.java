package com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tripagencymanagement.template.liquidations.infrastructure.entities.FlightBooking;

@Repository
public interface IFlightBookingJpaRepository extends JpaRepository<FlightBooking, Long> {
    
    List<FlightBooking> findByFlightServiceId(Long flightServiceId);
}
