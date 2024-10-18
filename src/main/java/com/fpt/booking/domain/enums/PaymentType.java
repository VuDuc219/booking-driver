package com.fpt.booking.domain.enums;

import lombok.Getter;

@Getter
public enum PaymentType {
    COD("COD"),
    ONLINE("ONLINE");
    private String value;
    PaymentType(String value) {
        this.value = value;
    }
}
