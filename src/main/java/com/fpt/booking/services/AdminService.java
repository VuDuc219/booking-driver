package com.fpt.booking.services;

import com.fpt.booking.domain.payload.request.VoucherRequest;
import com.fpt.booking.domain.payload.response.*;
import org.springframework.data.domain.Page;

import java.util.List;


public interface AdminService {
    Page<VoucherResponse> getVoucherResponses(Integer pageNo, Integer pageSize);
    VoucherResponse getVoucherById(Long id);
    MessageResponse createVoucher(VoucherRequest voucherRequest);
    MessageResponse updateVoucher(Long id, VoucherRequest voucherRequest);
    MessageResponse deleteVoucher(Long id);

    MessageResponse sendNotificationAllUser(Long id);
    Page<UserResponse> getPageOfReviewApplication(Integer pageNo, Integer pageSize);

    Page<UserResponse> getPageAll(Integer pageNo, Integer pageSize);

    Revenue sumOfRevenue();

    List<RevenueForMechanic> sumRevenueOfMechanic();

    List<RevenueOfMechanicForWeek> sumRevenueMechanicForWeek();

    Page<RequestTicketResponse> getPageRequestTicket(Integer pageNo, Integer pageSize);
}
