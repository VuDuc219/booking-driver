package com.fpt.booking.domain.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MessageResponse {
    private String message;

    public MessageResponse (String message) {
        this.message = message;
    }
}
