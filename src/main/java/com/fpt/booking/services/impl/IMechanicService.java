package com.fpt.booking.services.impl;

import com.fpt.booking.common.CommonMapper;
import com.fpt.booking.common.Constant;
import com.fpt.booking.config.ResourceBundleConfig;
import com.fpt.booking.domain.entities.FirebaseNotification;
import com.fpt.booking.domain.entities.Garage;
import com.fpt.booking.domain.entities.RequestTicket;
import com.fpt.booking.domain.entities.User;
import com.fpt.booking.domain.enums.RequestTicketsStatus;
import com.fpt.booking.domain.payload.request.GarageRequest;
import com.fpt.booking.domain.payload.request.InspectionResultsRequest;
import com.fpt.booking.domain.payload.request.RepairQuoteRequest;
import com.fpt.booking.domain.payload.response.MessageResponse;
import com.fpt.booking.domain.payload.response.RequestTicketResponse;
import com.fpt.booking.exception.BadRequestException;
import com.fpt.booking.repository.GarageRepository;
import com.fpt.booking.repository.RequestTicketRepository;
import com.fpt.booking.repository.UserRepository;
import com.fpt.booking.services.FirebaseService;
import com.fpt.booking.services.MechanicService;
import com.fpt.booking.utils.MessageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class IMechanicService extends BaseService implements MechanicService {

    private final GarageRepository garageRepository;
    private final UserRepository userRepository;
    private final ResourceBundleConfig resourceBundleConfig;
    private final RequestTicketRepository requestTicketRepository;
    private final FirebaseService firebaseService;
    private final CommonMapper commonMapper;
    @Override
    public MessageResponse createNewGarage(GarageRequest garageRequest) {
        User account = getAccountById();
        if (account.getMoto().getId() != null) {
            throw new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.ONLY_GARAGE));
        }

        Garage garage = new Garage(garageRequest, account);
        garageRepository.save(garage);

        String message = resourceBundleConfig.getViMessage(MessageUtils.CREATE_NEW_GARAGE, garageRequest.getName());
        return MessageResponse.builder().message(message).build();
    }

    @Override
    public MessageResponse updateInfoGarage(GarageRequest garageRequest) {
        Garage garage = getGarageByUserOrThrow();
        updateGarage(garage, garageRequest);
        garageRepository.save(garage);
        return MessageResponse.builder().message(resourceBundleConfig.getViMessage(MessageUtils.UPGRADE_GARAGE)).build();
    }

    @Override
    public MessageResponse deleteInfoGarage() {
        Garage garage = getGarageByUserOrThrow();
        garageRepository.findById(garage.getId());
        return MessageResponse.builder().message(resourceBundleConfig.getViMessage(MessageUtils.DELETE_GARAGE)).build();
    }

    @Override
    public void confirmAppointment(Long id) {
        RequestTicket requestTicket = requestTicketRepository.findById(id).orElseThrow(() -> new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.REQUEST_TICKET_NOT_FOUND)));

        FirebaseNotification firebaseNotification = new FirebaseNotification(LocalDateTime.now(), getUserId(),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_GARAGE_CONFIRM),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_GARAGE_CONFIRM_CONTENT,
                        getAccountById().getGarage().getName()));


        if (RequestTicketsStatus.CONFIRMED.equals(requestTicket.getStatus())) {
            throw new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.ERROR_GARAGE_CONFIRM));
        } else {
            requestTicket.setStatus(RequestTicketsStatus.CONFIRMED);
            requestTicket.setMechanic(getAccountById());
            requestTicketRepository.save(requestTicket);

            log.info("Driver ID:" +  requestTicket.getDriver().getId());
            firebaseService.sendNotificationToDevices(requestTicket.getDriver().getId(), firebaseNotification);
        }
    }

    @Override
    public MessageResponse  motoRepairProcess(Long id) {
        RequestTicket requestTicket = requestTicketRepository.findById(id).orElseThrow(() -> new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.REQUEST_TICKET_NOT_FOUND)));
        requestTicket.setStatus(RequestTicketsStatus.PROCESSING);
        log.info(Constant.REQUEST_TICKET_ID + requestTicket.getId());
        requestTicketRepository.save(requestTicket);

        FirebaseNotification firebaseNotification = new FirebaseNotification(LocalDateTime.now(), getUserId(),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_PROCESSING),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_PROCESSING_CONTENT,
                        requestTicket.getDriver().getPhone()));
        firebaseService.sendNotificationToDevices(requestTicket.getDriver().getId(), firebaseNotification);
        return MessageResponse.builder().message(resourceBundleConfig.getViMessage(MessageUtils.PROCESSING)).build();
    }

    @Override
    public MessageResponse fixedMoto(Long id) {
        RequestTicket requestTicket = requestTicketRepository.findById(id).orElseThrow(() -> new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.REQUEST_TICKET_NOT_FOUND)));
        requestTicket.setStatus(RequestTicketsStatus.PROCESSING);
        log.info(Constant.REQUEST_TICKET_ID + requestTicket.getId());
        requestTicketRepository.save(requestTicket);

        FirebaseNotification firebaseNotification = new FirebaseNotification(LocalDateTime.now(), getUserId(),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_FIXED),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_FIXED_CONTENT,
                        requestTicket.getMechanic().getGarage().getName()));
        firebaseService.sendNotificationToDevices(requestTicket.getDriver().getId(), firebaseNotification);

        return MessageResponse.builder().message(resourceBundleConfig.getViMessage(MessageUtils.FIXED)).build();
    }

    @Override
    public MessageResponse addVehicleInspectionResults(Long id, InspectionResultsRequest inspectionResultsRequest) {
        RequestTicket requestTicket = requestTicketRepository.findById(id).orElseThrow(() -> new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.REQUEST_TICKET_NOT_FOUND)));
        requestTicket.setDescriptionInspectionResults(inspectionResultsRequest.getDescriptionInspectionResults());
        requestTicket.setStatus(RequestTicketsStatus.PROCESSING);
        log.info(Constant.REQUEST_TICKET_ID + requestTicket.getId());
        requestTicketRepository.save(requestTicket);

        FirebaseNotification firebaseNotification = new FirebaseNotification(LocalDateTime.now(), getUserId(),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_INSPECTION_RESULTS),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_INSPECTION_RESULTS_CONTENT,
                        requestTicket.getDriver().getPhone()));
        firebaseService.sendNotificationToDevices(requestTicket.getDriver().getId(), firebaseNotification);
        return MessageResponse.builder().message(resourceBundleConfig.getViMessage(MessageUtils.INSPECTION_RESULTS)).build();
    }

    @Override
    public MessageResponse sendRepairQuoteForCustomer(Long id, RepairQuoteRequest repairQuoteRequest) {
        RequestTicket requestTicket = requestTicketRepository.findById(id).orElseThrow(() -> new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.REQUEST_TICKET_NOT_FOUND)));
        requestTicket.setVehicleHandoverTime(repairQuoteRequest.getVehicleHandoverTime());
        requestTicket.setPrice(repairQuoteRequest.getPrice());
        requestTicket.setStatus(RequestTicketsStatus.PROCESSING);
        log.info(Constant.REQUEST_TICKET_ID + requestTicket.getId());
        requestTicketRepository.save(requestTicket);

        FirebaseNotification firebaseNotification = new FirebaseNotification(LocalDateTime.now(), getUserId(),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_WAITING_CUSTOMER_APPROVE_PRICE),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_WAITING_CUSTOMER_APPROVE_PRICE_CONTENT,
                        requestTicket.getDriver().getPhone()));
        firebaseService.sendNotificationToDevices(requestTicket.getDriver().getId(), firebaseNotification);

        return MessageResponse.builder().message(resourceBundleConfig.getViMessage(MessageUtils.REPAIR_QUOTE)).build();
    }

    @Override
    @Transactional
    public MessageResponse editInfoOfVehicleInspectionResults(Long id, InspectionResultsRequest inspectionResultsRequest) {
        RequestTicket requestTicket = requestTicketRepository.findById(id).orElseThrow(() -> new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.REQUEST_TICKET_NOT_FOUND)));
        requestTicket.setDescriptionInspectionResults(inspectionResultsRequest.getDescriptionInspectionResults());
        log.info(Constant.REQUEST_TICKET_ID + requestTicket.getId());
        requestTicketRepository.save(requestTicket);

        FirebaseNotification firebaseNotification = new FirebaseNotification(LocalDateTime.now(), getUserId(),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_UPGRADE_INSPECTION_RESULT),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_UPGRADE_INSPECTION_RESULT_CONTENT,
                        requestTicket.getMechanic().getGarage().getName()));
        firebaseService.sendNotificationToDevices(requestTicket.getDriver().getId(), firebaseNotification);

        return MessageResponse.builder().message(resourceBundleConfig.getViMessage(MessageUtils.UPGRADE_INSPECTION_RESULT)).build();
    }

    @Override
    @Transactional
    public MessageResponse editInfoOfRepairQuote(Long id, RepairQuoteRequest repairQuoteRequest) {
        RequestTicket requestTicket = requestTicketRepository.findById(id).orElseThrow(() -> new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.REQUEST_TICKET_NOT_FOUND)));
        requestTicket.setVehicleHandoverTime(repairQuoteRequest.getVehicleHandoverTime());
        requestTicket.setPrice(repairQuoteRequest.getPrice());
        log.info(Constant.REQUEST_TICKET_ID + requestTicket.getId());
        requestTicketRepository.save(requestTicket);

        FirebaseNotification firebaseNotification = new FirebaseNotification(LocalDateTime.now(), getUserId(),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_UPGRADE_REPAIR_QUOTE),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_UPGRADE_REPAIR_QUOTE_CONTENT,
                        requestTicket.getMechanic().getGarage().getName()));

        firebaseService.sendNotificationToDevices(requestTicket.getDriver().getId(), firebaseNotification);

        return MessageResponse.builder().message(resourceBundleConfig.getViMessage(MessageUtils.UPGRADE_REPAIR_QUOTE)).build();
    }

    @Override
    public MessageResponse garageCanceledForRequestTicket(Long id) {
        RequestTicket requestTicket = requestTicketRepository.findById(id).orElseThrow(() -> new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.REQUEST_TICKET_NOT_FOUND)));
        requestTicket.setStatus(RequestTicketsStatus.CANCELED);
        requestTicketRepository.save(requestTicket);

        FirebaseNotification firebaseNotification = new FirebaseNotification(LocalDateTime.now(), getUserId(),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_GARAGE_CANCELED),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_GARAGE_CANCELED_CONTENT,
                        requestTicket.getMechanic().getGarage().getName()));
        firebaseService.sendNotificationToDevices(requestTicket.getDriver().getId(), firebaseNotification);
        return MessageResponse.builder().message(resourceBundleConfig.getViMessage(MessageUtils.CANCELED)).build();
    }

    @Override
    public MessageResponse garageHandoverMoto(Long id) {
        RequestTicket requestTicket = requestTicketRepository.findById(id).orElseThrow(() -> new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.REQUEST_TICKET_NOT_FOUND)));
        requestTicket.setStatus(RequestTicketsStatus.COMPLETED);
        requestTicketRepository.save(requestTicket);

        FirebaseNotification firebaseNotification = new FirebaseNotification(LocalDateTime.now(), getUserId(),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_HANDOVER_MOTO),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_HANDOVER_MOTO_CONTENT,
                        requestTicket.getMechanic().getGarage().getName()));
        firebaseService.sendNotificationToDevices(requestTicket.getDriver().getId(), firebaseNotification);
        return MessageResponse.builder().message(resourceBundleConfig.getViMessage(MessageUtils.HANDOVER_MOTO)).build();
    }

    @Override
    public MessageResponse confirmPayment(Long id) {
        RequestTicket requestTicket = requestTicketRepository.findById(id).orElseThrow(() -> new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.REQUEST_TICKET_NOT_FOUND)));
        requestTicket.setStatus(RequestTicketsStatus.PROCESSING);
        requestTicketRepository.save(requestTicket);

        FirebaseNotification firebaseNotification = new FirebaseNotification(LocalDateTime.now(), getUserId(),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_COMPLETED_PAYMENT),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_COMPLETED_PAYMENT_CONTENT,
                        requestTicket.getId()));
        firebaseService.sendNotificationToDevices(requestTicket.getDriver().getId(), firebaseNotification);
        return MessageResponse.builder().message(resourceBundleConfig.getViMessage(MessageUtils.COMPLETED_PAYMENT)).build();
    }

    @Override
    public Page<RequestTicketResponse> getAllRequestTicket(RequestTicketsStatus status, Integer pageNo, Integer pageSize) {
        int page = pageNo == 0 ? pageNo : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<RequestTicket> requestTickets = requestTicketRepository.findAllByMechanicOrStatus(getAccountById(), status, pageable);
        return commonMapper.convertToResponsePage(requestTickets, RequestTicketResponse.class, pageable);
    }


    private Garage getGarageByUserOrThrow() {
        return garageRepository.findByUser(getAccountById()).orElseThrow(() -> new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.GARAGE_EMPTY)));
    }

    private void updateGarage(Garage garage, GarageRequest garageRequest) {
        garage.setAddress(garageRequest.getAddress());
        garage.setName(garageRequest.getName());
        garage.setDescription(garageRequest.getDescription());
        garage.setLatiTude(garageRequest.getLatiTude());
        garage.setLongiTude(garageRequest.getLongiTude());
        garage.setUsername(garageRequest.getUsername());
    }

    private User getAccountById(){
        return  userRepository.findById(getUserId()).orElseThrow(() ->  new BadRequestException(this.resourceBundleConfig.getViMessage(MessageUtils.ACCOUNT_NOT_FOUND)));
    }
}
