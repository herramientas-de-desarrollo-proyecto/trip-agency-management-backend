package com.tripagencymanagement.template.liquidations.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tripagencymanagement.template.customers.domain.entities.DCustomer;
import com.tripagencymanagement.template.liquidations.domain.entities.DAdditionalServices;
import com.tripagencymanagement.template.liquidations.domain.entities.DFlightService;
import com.tripagencymanagement.template.liquidations.domain.entities.DHotelService;
import com.tripagencymanagement.template.liquidations.domain.entities.DIncidency;
import com.tripagencymanagement.template.liquidations.domain.entities.DPayment;
import com.tripagencymanagement.template.liquidations.domain.entities.DTourService;
import com.tripagencymanagement.template.liquidations.domain.enums.DLiquidationStatus;
import com.tripagencymanagement.template.liquidations.domain.enums.DPaymentStatus;
import com.tripagencymanagement.template.users.domain.entities.DStaff;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
public class LiquidationWithDetailsDto {
    private Long id;
    private Long customerId;
    private Long staffId;
    private DCustomer customer;
    private DStaff staffOnCharge;
    private float currencyRate;
    private float totalAmount;
    private Float totalAmountUSD;
    private Float totalCommissionPEN;
    private Float totalCommissionUSD;
    private LocalDateTime paymentDeadline;
    private int companion;
    private DLiquidationStatus status;
    private DPaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<DPayment> payments;
    private List<DFlightService> flightServices;
    private List<DHotelService> hotelServices;
    private List<DTourService> tourServices;
    private List<DAdditionalServices> additionalServices;
    private List<DIncidency> incidencies;
}
