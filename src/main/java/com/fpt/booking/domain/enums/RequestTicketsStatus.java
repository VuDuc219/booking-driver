package com.fpt.booking.domain.enums;

import lombok.Getter;

@Getter
public enum RequestTicketsStatus {
    NEW("NEW"),
    CONFIRMED("CONFIRMED"),
    PROCESSING("PROCESSING"),
    COMPLETED("COMPLETED"),
    CANCELED("CANCELED");

    private String value;

    RequestTicketsStatus(String value) {
        this.value = value;
    }

}