package com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tripagencymanagement.template.liquidations.infrastructure.entities.HotelBooking;

@Repository
public interface IHotelBookingJpaRepository extends JpaRepository<HotelBooking, Long> {
    
    List<HotelBooking> findByHotelServiceId(Long hotelServiceId);
}
