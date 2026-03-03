package com.tripagencymanagement.template.liquidations.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("customer_id")
    private Long customerId;

    @JsonProperty("staff_id")
    private Long staffId;

    private DCustomer customer;

    @JsonProperty("staff_on_charge")
    private DStaff staffOnCharge;

    @JsonProperty("currency_rate")
    private float currencyRate;

    @JsonProperty("total_amount")
    private float totalAmount;

    @JsonProperty("total_amount_usd")
    private Float totalAmountUSD;

    @JsonProperty("total_commission_pen")
    private Float totalCommissionPEN;

    @JsonProperty("total_commission_usd")
    private Float totalCommissionUSD;

    @JsonProperty("payment_deadline")
    private LocalDateTime paymentDeadline;

    private int companion;
    private DLiquidationStatus status;

    @JsonProperty("payment_status")
    private DPaymentStatus paymentStatus;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    private List<DPayment> payments;

    @JsonProperty("flight_services")
    private List<DFlightService> flightServices;

    @JsonProperty("hotel_services")
    private List<DHotelService> hotelServices;

    @JsonProperty("tour_services")
    private List<DTourService> tourServices;

    @JsonProperty("additional_services")
    private List<DAdditionalServices> additionalServices;

    private List<DIncidency> incidencies;
}
