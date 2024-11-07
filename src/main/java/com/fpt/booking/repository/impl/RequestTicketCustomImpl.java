package com.fpt.booking.repository.impl;

import com.fpt.booking.domain.entities.RequestTicket;
import com.fpt.booking.domain.entities.User;
import com.fpt.booking.domain.enums.RequestTicketsStatus;
import com.fpt.booking.repository.BaseRepository;
import com.fpt.booking.repository.RequestTicketCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestTicketCustomImpl implements RequestTicketCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<RequestTicket> findAllByMechanicAndStatus(User mechanic, RequestTicketsStatus requestTicketsStatus, Pageable pageable) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append( " FROM request_ticket rt WHERE 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        if (Objects.nonNull(mechanic.getId())) {
            strQuery.append(" AND rt.mechanic_id = :userId  ");
            params.put("userId", mechanic.getId());
        }

        if (Objects.nonNull(requestTicketsStatus)){
            strQuery.append(" AND rt.status = :status  ");
            params.put("status", requestTicketsStatus.getValue());
        }
        String selectQuery = "select * " + strQuery + " ORDER BY rt.id DESC";
        String strCountQuery = "SELECT COUNT(DISTINCT rt.id)" + strQuery;


        return BaseRepository.getPagedNativeQuery(em,selectQuery, strCountQuery, params, pageable, RequestTicket.class);

    }

    @Override
    public Page<RequestTicket> findAllByDriverAndStatus(User user, RequestTicketsStatus requestTicketsStatus, Pageable pageable) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append( " FROM request_ticket rt WHERE 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        if (Objects.nonNull(user.getId())) {
            strQuery.append(" AND rt.driver_id = :userId  ");
            params.put("userId", user.getId());
        }

        if (Objects.nonNull(requestTicketsStatus)){
            strQuery.append(" AND rt.status = :status  ");
            params.put("status", requestTicketsStatus.getValue());
        }
        String selectQuery = "select * " + strQuery + " ORDER BY rt.id DESC";
        String strCountQuery = "SELECT COUNT(DISTINCT rt.id)" + strQuery;


        return BaseRepository.getPagedNativeQuery(em,selectQuery, strCountQuery, params, pageable, RequestTicket.class);
    }
}
