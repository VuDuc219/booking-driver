package com.fpt.booking.services;

import com.fpt.booking.domain.payload.response.RequestTicketResponse;

public interface BookingService {
    RequestTicketResponse getInfoRequestTicket(Long id);
}
