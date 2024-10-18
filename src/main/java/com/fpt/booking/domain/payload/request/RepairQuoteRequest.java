package com.fpt.booking.domain.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RepairQuoteRequest {
    private Double price;
    private LocalDateTime vehicleHandoverTime;
}
