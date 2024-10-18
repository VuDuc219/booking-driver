package com.fpt.booking.services;

import com.fpt.booking.domain.enums.RequestTicketsStatus;
import com.fpt.booking.domain.payload.request.GarageRequest;
import com.fpt.booking.domain.payload.request.InspectionResultsRequest;
import com.fpt.booking.domain.payload.request.RepairQuoteRequest;
import com.fpt.booking.domain.payload.response.MessageResponse;
import com.fpt.booking.domain.payload.response.RequestTicketResponse;
import org.springframework.data.domain.Page;

public interface MechanicService {
    MessageResponse createNewGarage(GarageRequest garageRequest);

    MessageResponse updateInfoGarage(GarageRequest garageRequest);

    MessageResponse deleteInfoGarage();

    void confirmAppointment(Long id);

    MessageResponse motoRepairProcess(Long id);

    MessageResponse fixedMoto(Long id);


    MessageResponse addVehicleInspectionResults(Long id, InspectionResultsRequest inspectionResultsRequest);

    MessageResponse sendRepairQuoteForCustomer(Long id, RepairQuoteRequest repairQuoteRequest);

    MessageResponse editInfoOfVehicleInspectionResults(Long id, InspectionResultsRequest inspectionResultsRequest);

    MessageResponse editInfoOfRepairQuote(Long id, RepairQuoteRequest repairQuoteRequest);

    MessageResponse garageCanceledForRequestTicket(Long id);

    MessageResponse garageHandoverMoto(Long id);

    MessageResponse confirmPayment(Long id);

    Page<RequestTicketResponse> getAllRequestTicket(RequestTicketsStatus status, Integer pageNo, Integer pageSize);
}
