package com.fpt.booking.domain.payload.response;


import com.fpt.booking.domain.enums.PaymentType;
import com.fpt.booking.domain.enums.RequestTicketType;
import com.fpt.booking.domain.enums.RequestTicketsStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RequestTicketResponse {

    private Long id;

    private UserResponse driver;

    private UserResponse mechanic;

    private String motorbikeSpareParts;

    private String description;

    private Double price;

    private RequestTicketsStatus status;

    private RequestTicketType type;

    private PaymentType paymentType;

    private LocalDateTime appointmentTime;

    private String formattedAppointmentDate;
}
