package com.fpt.booking.domain.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VoucherResponse {
    private Long id;
    private String title;
    private Integer discount;
    private String code;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
