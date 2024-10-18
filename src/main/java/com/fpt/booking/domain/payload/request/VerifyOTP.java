package com.fpt.booking.domain.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOTP {
    private String email;
    private Integer otp;
    private String type;
}
