package com.fpt.booking.domain.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GarageRequest {
    private String name;
    private String username;
    private String address;
    private Float latiTude;
    private Float longiTude;
    private String description;
}
