package com.fpt.booking.services.impl;

import com.fpt.booking.common.CommonMapper;
import com.fpt.booking.common.Constant;
import com.fpt.booking.config.ResourceBundleConfig;
import com.fpt.booking.domain.entities.FirebaseNotification;
import com.fpt.booking.domain.entities.Moto;
import com.fpt.booking.domain.entities.RequestTicket;
import com.fpt.booking.domain.entities.User;
import com.fpt.booking.domain.enums.PaymentType;
import com.fpt.booking.domain.enums.RequestTicketType;
import com.fpt.booking.domain.enums.RequestTicketsStatus;
import com.fpt.booking.domain.payload.request.MotoRequest;
import com.fpt.booking.domain.payload.request.PaymentRequest;
import com.fpt.booking.domain.payload.request.RequestTicketDTO;
import com.fpt.booking.domain.payload.response.GarageResponse;
import com.fpt.booking.domain.payload.response.LongResponse;
import com.fpt.booking.domain.payload.response.MessageResponse;
import com.fpt.booking.domain.payload.response.RequestTicketResponse;
import com.fpt.booking.exception.BadRequestException;
import com.fpt.booking.repository.GarageRepository;
import com.fpt.booking.repository.MotoRepository;
import com.fpt.booking.repository.RequestTicketRepository;
import com.fpt.booking.repository.UserRepository;
import com.fpt.booking.services.FirebaseService;
import com.fpt.booking.services.UserService;
import com.fpt.booking.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IUserService extends BaseService implements UserService {

    private final UserRepository userRepository;
    private final GarageRepository garageRepository;
    private final MotoRepository motoRepository;
    private final CommonMapper commonMapper;
    private final ResourceBundleConfig resourceBundleConfig;
    private final RequestTicketRepository requestTicketRepository;
    private final FirebaseService firebaseService;

    @Override
    public List<GarageResponse> findMechanicInRadius(Double latitude, Double longitude, String type) {
        if ("DISTACE".equals(type)) {
            return garageRepository.findAll().stream()
                    .filter(garage -> calculateDistance(latitude, longitude, garage.getLatiTude(), garage.getLongiTude()) <= 5.0)
                    .map(garage -> {
                        GarageResponse garageResponse = commonMapper.convertToResponse(garage, GarageResponse.class);
                        garageResponse.setDistance(calculateDistance(latitude, longitude, garage.getLatiTude(), garage.getLongiTude()));
                        garageResponse.setMechanicId(garage.getUser().getId());
                        return garageResponse;
                    })
                    .collect(Collectors.toList());
        } else {
            return garageRepository.findAll().stream()
                    .map(garage -> {
                        GarageResponse garageResponse = commonMapper.convertToResponse(garage, GarageResponse.class);
                        garageResponse.setDistance(0.0);
                        garageResponse.setMechanicId(garage.getUser().getId());
                        return garageResponse;
                    })
                    .collect(Collectors.toList());
        }
    }

    @Override
    public MessageResponse createNewMoto(MotoRequest motoRequest) {
        User account = getAccountById();
        if (account.getMoto() != null) {
            throw new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.ONLY_MOTO));
        }

        Moto newMoto = new Moto(motoRequest, account);
        motoRepository.save(newMoto);

        String message = resourceBundleConfig.getViMessage(MessageUtils.CREATE_NEW_MOTO, motoRequest.getLicensePlate());
        return MessageResponse.builder().message(message).build();
    }

    @Override
    public MessageResponse updateInfoOfMoto(MotoRequest motoRequest) {
        Moto moto = getMotoByUserOrThrow();
        updateMotoInfo(moto, motoRequest);
        motoRepository.save(moto);
        return MessageResponse.builder().message(resourceBundleConfig.getViMessage(MessageUtils.UPGRADE_INFO_MOTO, moto.getLicensePlate())).build();
    }

    @Override
    public MessageResponse deleteInfoOfMoto() {
        Moto moto = getMotoByUserOrThrow();
        motoRepository.deleteMotoOfCustomer(moto.getId());
        return MessageResponse.builder().message(resourceBundleConfig.getViMessage(MessageUtils.DELETE_MOTO, moto.getLicensePlate())).build();
    }

    @Override
    public LongResponse createNewAppointment(RequestTicketDTO requestTicketDTO) {

        RequestTicket requestTicket = new RequestTicket(getAccountById(),
                requestTicketDTO.getMotorbikeSpareParts(),
                requestTicketDTO.getDescription(),
                RequestTicketsStatus.NEW,
                requestTicketDTO.getType(),
                RequestTicketType.SOS.equals(requestTicketDTO.getType()) ? LocalDateTime.now() : requestTicketDTO.getAppointmentDate());

        FirebaseNotification firebaseNotification = new FirebaseNotification(LocalDateTime.now(), getUserId(),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_NEW_APPOINTMENT),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_NEW_APPOINTMENT_CONTENT,
                        getAccountById().getMoto().getLicensePlate(),
                        getAccountById().getPhone(),
                        requestTicketDTO.getType()));

        if (RequestTicketType.SOS.equals(requestTicketDTO.getType())){
            for (GarageResponse g : findMechanicInRadius(requestTicketDTO.getLatiTude(), requestTicketDTO.getLongiTude(),"DISTANCE")) {
                log.info("Mechanic ID" + g.getMechanicId());
                firebaseService.sendNotificationToDevices(g.getMechanicId(), firebaseNotification);
            }
        } else {
            if (requestTicketDTO.getMechanicId() == null)
                throw new BadRequestException("Thiếu thông tin của mechanicID");
            User mechanic = userRepository.findById(requestTicketDTO.getMechanicId()).orElseThrow(() -> new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.GARAGE_WITH_TICKET_TYPE)));
            requestTicket.setMechanic(mechanic);
            firebaseService.sendNotificationToDevices(requestTicketDTO.getMechanicId(), firebaseNotification);
        }
        requestTicketRepository.save(requestTicket);
        return LongResponse.builder().requestTicketId(requestTicket.getId()).build();
    }

    @Override
    public MessageResponse checkIn(Long id) {
        RequestTicket requestTicket = requestTicketRepository.findById(id).orElseThrow(() -> new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.REQUEST_TICKET_NOT_FOUND)));
        requestTicket.setStatus(RequestTicketsStatus.CUSTOMER_CHECKED_IN);
        log.info(Constant.REQUEST_TICKET_ID + requestTicket.getId());
        requestTicketRepository.save(requestTicket);

        FirebaseNotification firebaseNotification = new FirebaseNotification(LocalDateTime.now(), getUserId(),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_CHECKIN),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_CHECKIN_CONTENT,
                        requestTicket.getDriver().getPhone()));

        firebaseService.sendNotificationToDevices(requestTicket.getMechanic().getId(), firebaseNotification);
        return MessageResponse.builder().message(resourceBundleConfig.getViMessage(MessageUtils.CHECKIN)).build();
    }

    @Override
    public MessageResponse approvePriceOfMechanic(Long id) {
        RequestTicket requestTicket = requestTicketRepository.findById(id).orElseThrow(() -> new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.REQUEST_TICKET_NOT_FOUND)));
        requestTicket.setStatus(RequestTicketsStatus.CUSTOMER_APPROVED_PRICE);
        log.info(Constant.REQUEST_TICKET_ID + requestTicket.getId());
        requestTicketRepository.save(requestTicket);

        FirebaseNotification firebaseNotification = new FirebaseNotification(LocalDateTime.now(), getUserId(),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_CUSTOMER_APPROVED_PRICE),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_CUSTOMER_APPROVED_PRICE_CONTENT,
                        requestTicket.getDriver().getPhone(),
                        requestTicket.getId()));

        firebaseService.sendNotificationToDevices(requestTicket.getMechanic().getId(), firebaseNotification);
        return MessageResponse.builder().message(resourceBundleConfig.getViMessage(MessageUtils.APPROVE_PRICE)).build();
    }

    @Override
    public MessageResponse paymentForGarage(Long id, PaymentRequest paymentRequest) {
        RequestTicket requestTicket = requestTicketRepository.findById(id).orElseThrow(() -> new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.REQUEST_TICKET_NOT_FOUND)));
        requestTicket.setStatus(RequestTicketsStatus.PENDING_PAYMENT);
        User mechanic = userRepository.findById(requestTicket.getMechanic().getId()).orElseThrow(() -> new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.USER_NOT_FOUND)));
        User user = getAccountById();
        if (!PaymentType.COD.equals(paymentRequest.getPaymentType()) && user.getTotalMoney() > paymentRequest.getPrice()){
            user.setTotalMoney(user.getTotalMoney() - paymentRequest.getPrice());
            mechanic.setTotalMoney(mechanic.getTotalMoney() + paymentRequest.getPrice());
        } else {
            throw new BadRequestException("Số dư của bạn không đủ để thanh toán");
        }

        FirebaseNotification firebaseNotification = new FirebaseNotification(LocalDateTime.now(), getUserId(),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_PAYMENT),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_PAYMENT_CONTENT,
                        requestTicket.getDriver().getPhone(),
                        paymentRequest.getPrice()));

        firebaseService.sendNotificationToDevices(requestTicket.getMechanic().getId(), firebaseNotification);

        return MessageResponse.builder().message("Thanh toán thành công. Hãy đợi thông báo trả xe của garage.").build();
    }

    @Override
    public MessageResponse completedRequestTicket(Long id) {
        RequestTicket requestTicket = requestTicketRepository.findById(id).orElseThrow(() -> new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.REQUEST_TICKET_NOT_FOUND)));
        requestTicket.setStatus(RequestTicketsStatus.COMPLETED);
        requestTicketRepository.save(requestTicket);

        FirebaseNotification firebaseNotification = new FirebaseNotification(LocalDateTime.now(), getUserId(),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_COMPLETED),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_COMPLETED_CONTENT,
                        requestTicket.getDriver().getPhone(),
                        requestTicket.getId()));

        firebaseService.sendNotificationToDevices(requestTicket.getMechanic().getId(), firebaseNotification);
        return MessageResponse.builder().message(resourceBundleConfig.getViMessage(MessageUtils.COMPLETED)).build();
    }

    @Override
    public MessageResponse canceledRequestTicket(Long id) {
        RequestTicket requestTicket = requestTicketRepository.findById(id).orElseThrow(() -> new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.REQUEST_TICKET_NOT_FOUND)));
        if (RequestTicketsStatus.CUSTOMER_APPROVED_PRICE.equals(requestTicket.getStatus()))
            throw new BadRequestException("Bạn không thể hủy phiếu. Vì xe của bạn đã bắt đầu sửa chữa.");
        requestTicket.setStatus(RequestTicketsStatus.CANCELED);
        requestTicketRepository.save(requestTicket);

        FirebaseNotification firebaseNotification = new FirebaseNotification(LocalDateTime.now(), getUserId(),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_CANCELED),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_CANCELED_CONTENT,
                        requestTicket.getDriver().getPhone(),
                        requestTicket.getId()));

        firebaseService.sendNotificationToDevices(requestTicket.getMechanic().getId(), firebaseNotification);
        return MessageResponse.builder().message(resourceBundleConfig.getViMessage(MessageUtils.CANCELED)).build();
    }

    @Override
    public Page<RequestTicketResponse> getAllRequestTicket(RequestTicketsStatus status, Integer pageNo, Integer pageSize) {
        int page = pageNo == 0 ? pageNo : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<RequestTicket> requestTickets = requestTicketRepository.findAllByDriverOrStatus(getAccountById(), status, pageable);
        return commonMapper.convertToResponsePage(requestTickets, RequestTicketResponse.class, pageable);
    }

    @Override
    public MessageResponse requestUpgradeBecomeMechanic() {
        User user = getAccountById();
        user.setIsMechanic(true);
        userRepository.save(user);
        return MessageResponse.builder().message("Tạo yêu cầu cho admin thành công.").build();
    }

    private Moto getMotoByUserOrThrow() {
        return motoRepository.findByUser(getAccountById())
                .orElseThrow(() -> new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.MOTO_EMPTY)));
    }

    private void updateMotoInfo(Moto moto, MotoRequest motoRequest) {
        moto.setBodyStyle(motoRequest.getBodyStyle());
        moto.setLicensePlate(motoRequest.getLicensePlate());
        moto.setBrand(motoRequest.getBrand());
        moto.setColor(motoRequest.getColor());
        moto.setOrigin(motoRequest.getOrigin());
        moto.setMachineNumber(moto.getMachineNumber());
        moto.setFrameNumber(moto.getFrameNumber());
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return Constant.EARTH_RADIUS * c;
    }

    private User getAccountById() {
        return userRepository.findById(getUserId()).orElseThrow(() -> new BadRequestException(this.resourceBundleConfig.getViMessage(MessageUtils.ACCOUNT_NOT_FOUND)));
    }
}
