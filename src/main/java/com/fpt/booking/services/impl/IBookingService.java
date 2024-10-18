package com.fpt.booking.services.impl;

import com.fpt.booking.common.CommonMapper;
import com.fpt.booking.config.ResourceBundleConfig;
import com.fpt.booking.domain.payload.response.RequestTicketResponse;
import com.fpt.booking.exception.BadRequestException;
import com.fpt.booking.repository.RequestTicketRepository;
import com.fpt.booking.services.BookingService;
import com.fpt.booking.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IBookingService implements BookingService {

    private final RequestTicketRepository requestTicketRepository;
    private final ResourceBundleConfig resourceBundleConfig;
    private final CommonMapper commonMapper;

    @Override
    public RequestTicketResponse getInfoRequestTicket(Long id) {
        return commonMapper.convertToEntity(requestTicketRepository.findById(id).orElseThrow(() ->
                new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.REQUEST_TICKET_NOT_FOUND))), RequestTicketResponse.class);
    }
}
