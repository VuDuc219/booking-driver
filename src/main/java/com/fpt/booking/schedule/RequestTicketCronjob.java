package com.fpt.booking.schedule;

import com.fpt.booking.domain.entities.RequestTicket;
import com.fpt.booking.domain.enums.RequestTicketsStatus;
import com.fpt.booking.repository.RequestTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class RequestTicketCronjob {
    private final RequestTicketRepository requestTicketRepository;

    //Cronjob after 2 minute if request ticket by NEW , change status by GARAGE_NO_ACTION
//    @Scheduled(cron = "0 */2 * * * *")
//    private void updateRequestTicketWhenGarageNoAction(){
//        List<RequestTicket> requestTickets = requestTicketRepository.findAll();
//        for (RequestTicket rt: requestTickets) {
//            if (RequestTicketsStatus.NEW.equals(rt.getStatus())){
//                rt.setStatus(RequestTicketsStatus.GARAGE_NO_ACTION);
//                requestTicketRepository.save(rt);
//            }
//
//            if (RequestTicketsStatus.GARAGE_CONFIRMED.equals(rt.getStatus())){
//                rt.setStatus(RequestTicketsStatus.CHECKING);
//                requestTicketRepository.save(rt);
//            }
//        }
//    }
}