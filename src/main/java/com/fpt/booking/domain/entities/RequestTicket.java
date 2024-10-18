package com.fpt.booking.domain.entities;

import com.fpt.booking.domain.enums.PaymentType;
import com.fpt.booking.domain.enums.RequestTicketType;
import com.fpt.booking.domain.enums.RequestTicketsStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "request_ticket")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private User driver;

    @ManyToOne
    @JoinColumn(name = "mechanic_id")
    private User mechanic;

    @Column(name = "motorbike_spare_parts")
    private String motorbikeSpareParts;

    private String description;

    @Column(name = "description_inspection_results")
    private String descriptionInspectionResults;

    @Column(name = "vehicle_handover_time")
    private LocalDateTime vehicleHandoverTime;

    private Double price;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequestTicketsStatus status;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private RequestTicketType type;

    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "appointment_time")
    private LocalDateTime appointmentTime;

    public RequestTicket(User driver,  String motorbikeSpareParts, String description, RequestTicketsStatus status, RequestTicketType type, LocalDateTime appointmentTime) {
        this.driver = driver;
        this.motorbikeSpareParts = motorbikeSpareParts;
        this.description = description;
        this.status = status;
        this.type = type;
        this.appointmentTime = appointmentTime;
    }
}
