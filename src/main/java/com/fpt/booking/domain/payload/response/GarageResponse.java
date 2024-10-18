package com.fpt.booking.domain.payload.response;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GarageResponse {
    private Long id;
    private String name;
    private String username;
    private String address;
    private Float latiTude;
    private Float longiTude;
    private String description;
    private Double distance;
    private Long mechanicId;
}
