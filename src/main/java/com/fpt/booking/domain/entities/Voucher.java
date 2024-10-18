package com.fpt.booking.domain.entities;

import com.fpt.booking.domain.enums.DateAudit;
import com.fpt.booking.domain.payload.request.VoucherRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "voucher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voucher extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Integer discount;
    private String code;
    private String content;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    private Long userId;

    public Voucher(VoucherRequest voucherRequest, Long userId) {
        this.title = voucherRequest.getTitle();
        this.discount = voucherRequest.getDiscount();
        this.content = voucherRequest.getContent();
        this.startDate = voucherRequest.getStartDate();
        this.endDate = voucherRequest.getEndDate();
        this.userId = userId;
    }
}
