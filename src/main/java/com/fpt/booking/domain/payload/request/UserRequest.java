package com.fpt.booking.domain.payload.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    @Email
    private String email;
    private String name;
    private String address;
}
