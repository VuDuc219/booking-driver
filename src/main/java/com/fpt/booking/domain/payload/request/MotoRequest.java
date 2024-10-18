package com.fpt.booking.domain.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MotoRequest {
    @NotBlank
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
