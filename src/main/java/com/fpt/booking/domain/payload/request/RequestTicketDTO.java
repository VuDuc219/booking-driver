package com.fpt.booking.domain.payload.request;

import com.fpt.booking.domain.enums.RequestTicketType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestTicketDTO {

    private Long mechanicId;

    private String motorbikeSpareParts;

    private String description;

    private Double latiTude;

    private Double longiTude;

    private String address;

    private LocalDateTime appointmentDate;

    private RequestTicketType type;
}
