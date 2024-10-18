package com.fpt.booking.domain.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class LongResponse {
    private Long requestTicketId;
}
