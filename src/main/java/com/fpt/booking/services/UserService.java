package com.fpt.booking.services;

import com.fpt.booking.domain.enums.RequestTicketsStatus;
import com.fpt.booking.domain.payload.request.MotoRequest;
import com.fpt.booking.domain.payload.request.PaymentRequest;
import com.fpt.booking.domain.payload.request.RequestTicketDTO;
import com.fpt.booking.domain.payload.response.GarageResponse;
import com.fpt.booking.domain.payload.response.LongResponse;
import com.fpt.booking.domain.payload.response.MessageResponse;
import com.fpt.booking.domain.payload.response.RequestTicketResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    List<GarageResponse> findMechanicInRadius(Double latitude, Double longitude, String type);

    MessageResponse createNewMoto(MotoRequest motoRequest);

    MessageResponse updateInfoOfMoto(MotoRequest motoRequest);

    MessageResponse deleteInfoOfMoto();

    LongResponse createNewAppointment(RequestTicketDTO requestTicketDTO);

    MessageResponse checkIn(Long id);

    MessageResponse approvePriceOfMechanic(Long id);

    MessageResponse paymentForGarage(Long id, PaymentRequest paymentRequest);

    MessageResponse completedRequestTicket(Long id);

    MessageResponse canceledRequestTicket(Long id);

    Page<RequestTicketResponse> getAllRequestTicket(RequestTicketsStatus status, Integer pageNo, Integer pageSize);

    MessageResponse requestUpgradeBecomeMechanic();
}
