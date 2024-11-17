package com.fpt.booking.services.impl;

import com.fpt.booking.common.CommonMapper;
import com.fpt.booking.common.Constant;
import com.fpt.booking.config.ResourceBundleConfig;
import com.fpt.booking.domain.entities.FirebaseNotification;
import com.fpt.booking.domain.entities.User;
import com.fpt.booking.domain.entities.Voucher;
import com.fpt.booking.domain.payload.request.VoucherRequest;
import com.fpt.booking.domain.payload.response.*;
import com.fpt.booking.exception.BadRequestException;
import com.fpt.booking.repository.RequestTicketRepository;
import com.fpt.booking.repository.UserRepository;
import com.fpt.booking.repository.VoucherRepository;
import com.fpt.booking.services.AdminService;
import com.fpt.booking.services.FirebaseService;
import com.fpt.booking.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IAdminService extends BaseService implements AdminService {
    private final VoucherRepository voucherRepository;
    private final CommonMapper commonMapper;
    private final ResourceBundleConfig resourceBundleConfig;
    private final UserRepository userRepository;
    private final FirebaseService firebaseService;
    private final RequestTicketRepository requestTicketRepository;
    @Override
    public Page<VoucherResponse> getVoucherResponses(Integer pageNo, Integer pageSize) {
        int page = pageNo == 0 ? pageNo : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Voucher> voucherResponses = new PageImpl<>(voucherRepository.findAll(), pageable, voucherRepository.findAll().size());
        return commonMapper.convertToResponsePage(voucherResponses, VoucherResponse.class, pageable);
    }

    @Override
    public VoucherResponse getVoucherById(Long id) {
        return commonMapper.convertToResponse(voucherRepository.findById(id), VoucherResponse.class);
    }

    @Override
    public MessageResponse createVoucher(VoucherRequest voucherRequest) {
        Voucher voucher = commonMapper.convertToEntity(voucherRequest, Voucher.class);
        voucher.setId(getUserId());
        voucherRepository.save(voucher);
        return MessageResponse.builder().message(resourceBundleConfig.getViMessage(MessageUtils.CREATE_VOUCHER)).build();
    }

    @Override
    public MessageResponse updateVoucher(Long id, VoucherRequest voucherRequest) {
        Voucher voucher = voucherRepository.findById(id).orElseThrow(() -> new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.VOUCHER_NOT_FOUND)));
        voucher.setTitle(voucherRequest.getTitle());
        voucher.setContent(voucherRequest.getContent());
        voucher.setDiscount(voucherRequest.getDiscount());
        voucher.setCode(voucherRequest.getCode());
        voucher.setStartDate(voucherRequest.getStartDate());
        voucher.setEndDate(voucherRequest.getEndDate());
        voucherRepository.save(voucher);
        return MessageResponse.builder().message(resourceBundleConfig.getViMessage(MessageUtils.UPDATE_VOUCHER)).build();
    }

    @Override
    public MessageResponse deleteVoucher(Long id) {
        voucherRepository.deleteById(id);
        return MessageResponse.builder().message(resourceBundleConfig.getViMessage(MessageUtils.DELETE_VOUCHER)).build();
    }

    @Override
    public MessageResponse sendNotificationAllUser(Long id) {
        Voucher voucher = voucherRepository.findById(id).orElseThrow(() -> new BadRequestException(resourceBundleConfig.getViMessage(MessageUtils.VOUCHER_NOT_FOUND)));
        FirebaseNotification firebaseNotification = new FirebaseNotification(LocalDateTime.now(), getUserId(),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_SEND_VOUCHER),
                resourceBundleConfig.getViMessage(MessageUtils.PUSH_NOTIFICATION_SEND_VOUCHER_CONTENT,
                        voucher.getTitle(),
                        voucher.getDiscount(),
                        voucher.getCode()));

        for (User user : userRepository.findAll()) {
            if (!user.getPhone().equals(Constant.PHONE_ADMIN)) {
                firebaseService.sendNotificationToDevices(user.getId(), firebaseNotification);
            }
        }
        return MessageResponse.builder().message(resourceBundleConfig.getViMessage(MessageUtils.SEND_VOUCHER)).build();
    }

    @Override
    public Page<UserResponse> getPageOfReviewApplication(Integer pageNo, Integer pageSize) {
        int page = pageNo == 0 ? pageNo : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        List<User> mechanicUsers = userRepository.findAll().stream()
                .filter(User::getIsMechanic)
                .toList();
        Page<User> users = new PageImpl<>(mechanicUsers, pageable, mechanicUsers.size());
        return commonMapper.convertToResponsePage(users, UserResponse.class, pageable);
    }

    @Override
    public Page<UserResponse> getPageAll(Integer pageNo, Integer pageSize) {
        int page = pageNo == 0 ? pageNo : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        List<User> mechanicUsers = userRepository.findAll().stream()
                .filter(i -> !Objects.equals(i.getEmail(), "admin@gmail.com"))
                .toList();
        Page<User> users = new PageImpl<>(mechanicUsers, pageable, mechanicUsers.size());
        return commonMapper.convertToResponsePage(users, UserResponse.class, pageable);
    }

    @Override
    public Revenue sumOfRevenue() {
        return requestTicketRepository.sumOfRevenue();
    }

    @Override
    public List<RevenueForMechanic> sumRevenueOfMechanic() {
        return requestTicketRepository.sumRevenueOfMechanic();
    }

    @Override
    public List<RevenueOfMechanicForWeek> sumRevenueMechanicForWeek() {
        return requestTicketRepository.sumRevenueMechanicForWeek();
    }

    @Override
    public Page<RequestTicketResponse> getPageRequestTicket(Integer pageNo, Integer pageSize) {
        int page = pageNo == 0 ? pageNo : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        return commonMapper.convertToResponsePage(requestTicketRepository.findAll(pageable), RequestTicketResponse.class, pageable);
    }
}
