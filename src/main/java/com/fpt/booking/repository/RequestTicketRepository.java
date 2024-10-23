package com.fpt.booking.repository;

import com.fpt.booking.domain.entities.RequestTicket;
import com.fpt.booking.domain.entities.User;
import com.fpt.booking.domain.enums.RequestTicketsStatus;
import com.fpt.booking.domain.payload.response.Revenue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestTicketRepository extends JpaRepository<RequestTicket, Long> , RequestTicketCustom{
    @Query(value = "SELECT SUM(rt.price) as sumOfRevenue FROM RequestTicket rt")
    Revenue sumOfRevenue();
}
