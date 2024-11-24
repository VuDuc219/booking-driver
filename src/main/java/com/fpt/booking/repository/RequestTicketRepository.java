package com.fpt.booking.repository;

import com.fpt.booking.domain.entities.RequestTicket;
import com.fpt.booking.domain.entities.User;
import com.fpt.booking.domain.enums.RequestTicketsStatus;
import com.fpt.booking.domain.payload.response.Revenue;
import com.fpt.booking.domain.payload.response.RevenueForMechanic;
import com.fpt.booking.domain.payload.response.RevenueOfMechanicForWeek;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestTicketRepository extends JpaRepository<RequestTicket, Long> , RequestTicketCustom{
    @Query(value = "SELECT SUM(rt.price) as sumOfRevenue FROM RequestTicket rt")
    Revenue sumOfRevenue();

    @Query(value = "SELECT u.name as name, SUM(rt.price) as sumOfRevenue FROM request_ticket rt " +
                   "INNER JOIN users u ON rt.mechanic_id = u.id " +
                   "GROUP BY rt.mechanic_id  ", nativeQuery = true )
    List<RevenueForMechanic> sumRevenueOfMechanic();

    @Query(value = "SELECT " +
            " u.name AS name, " +
            " YEAR(rt.created_at) AS year, " +
            " MONTH(rt.created_at) AS month, " +
            " WEEK(rt.created_at, 1) AS week, " +
            " SUM(rt.price) AS sumOfRevenue, " +
            " COUNT(rt.id) AS numOfRq " +
            "FROM " +
            " request_ticket rt " +
            "INNER JOIN users u ON " +
            " rt.mechanic_id = u.id " +
            "WHERE " +
            " rt.created_at >=  DATE_ADD(NOW(), INTERVAL -7 DAY) " +
            " AND rt.created_at < NOW() " +
            "GROUP BY " +
            " u.name, " +
            " YEAR(rt.created_at), " +
            " MONTH(rt.created_at), " +
            " WEEK(rt.created_at, 1) " +
            "ORDER BY " +
            " week; ", nativeQuery = true )
    List<RevenueOfMechanicForWeek> sumRevenueMechanicForWeek();


    @Query(value = "SELECT " +
            "    u.name AS name, " +
            "    DATE(rt.created_at) AS date,  " +
            "    SUM(rt.price) AS sumOfRevenue, " +
            "    COUNT(rt.id) AS numOfRq " +
            "FROM " +
            "    request_ticket rt " +
            "INNER JOIN users u ON " +
            "    rt.mechanic_id = u.id " +
            "WHERE " +
            "    rt.created_at >= DATE_ADD(NOW(), INTERVAL -1 DAY) " +
            "    AND rt.created_at < NOW() " +
            "    AND rt.status = 'COMPLETED' " +
            "GROUP BY " +
            "    u.name, " +
            "    DATE(rt.created_at)  " +
            "ORDER BY " +
            "    date;", nativeQuery = true )
    List<RevenueOfMechanicForWeek> sumRevenueMechanicForDate();


    @Query(value = "SELECT " +
            "    u.name AS name, " +
            "    YEAR(rt.created_at) AS year,   " +
            "    SUM(rt.price) AS sumOfRevenue, " +
            "    COUNT(rt.id) AS numOfRq " +
            "FROM " +
            "    request_ticket rt " +
            "INNER JOIN users u ON " +
            "    rt.mechanic_id = u.id " +
            "WHERE " +
            "    rt.created_at >= DATE_FORMAT(NOW(), '%Y-01-01')  " +
            "    AND rt.created_at < NOW() " +
            "    AND rt.status = 'COMPLETED' " +
            "GROUP BY " +
            "    u.name, " +
            "    YEAR(rt.created_at)  " +
            "ORDER BY " +
            "    year;", nativeQuery = true )
    List<RevenueOfMechanicForWeek> sumRevenueMechanicForYear();

}
