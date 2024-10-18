package com.fpt.booking.domain.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MotoResponse {
    private Long id;
    private String licensePlate;
    private String brand;
    private String color;
    private String bodyStyle;
    private String carImage;
    private String origin;
    private String machineNumber;
    private String frameNumber;
    private Integer year;
}
