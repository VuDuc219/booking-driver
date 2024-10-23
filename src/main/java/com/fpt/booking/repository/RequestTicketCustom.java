package com.fpt.booking.repository;

import com.fpt.booking.domain.entities.RequestTicket;
import com.fpt.booking.domain.entities.User;
import com.fpt.booking.domain.enums.RequestTicketsStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RequestTicketCustom {
    Page<RequestTicket> findAllByMechanicAndStatus(User mechanic, RequestTicketsStatus requestTicketsStatus, Pageable pageable);

    Page<RequestTicket> findAllByDriverAndStatus(User user, RequestTicketsStatus requestTicketsStatus, Pageable pageable);


}
