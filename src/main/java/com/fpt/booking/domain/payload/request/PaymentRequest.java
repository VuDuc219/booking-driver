package com.fpt.booking.domain.payload.request;

import com.fpt.booking.domain.enums.PaymentType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private PaymentType paymentType;
    private Double price;
}
