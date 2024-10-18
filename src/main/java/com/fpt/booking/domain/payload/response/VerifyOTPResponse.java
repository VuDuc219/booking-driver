package com.fpt.booking.domain.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyOTPResponse {
    private Integer otp;
}
