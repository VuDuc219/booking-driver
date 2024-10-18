package com.fpt.booking.domain.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VoucherRequest {
    private String title;
    private Integer discount;
    private String content;
    private String code;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
