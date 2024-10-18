package com.fpt.booking.repository;

import com.fpt.booking.domain.entities.RequestTicket;
import com.fpt.booking.domain.entities.User;
import com.fpt.booking.domain.enums.RequestTicketsStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestTicketRepository extends JpaRepository<RequestTicket, Long> {

    Page<RequestTicket> findAllByMechanicOrStatus(User mechanic, RequestTicketsStatus requestTicketsStatus, Pageable pageable);

    Page<RequestTicket> findAllByDriverOrStatus(User user, RequestTicketsStatus requestTicketsStatus, Pageable pageable);
}
