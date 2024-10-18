package com.fpt.booking.domain.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    private Integer otp;
    private String newPassword;
    private String confirmedPassword;
}
