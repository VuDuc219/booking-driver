package com.fpt.booking.domain.enums;

import lombok.Getter;

@Getter
public enum RequestTicketType {
    SOS("SOS"),
    REPAIR("REPAIR"),
    MAINTENANCE("MAINTENANCE"),
    UPGRADE("UPGRADE"),
    PAINT("PAINT"),
    SPA("SPA");

    private String value;

    RequestTicketType(String value) {
        this.value = value;
    }

}