package com.fpt.booking.controller;

import com.fpt.booking.controller.base.BaseController;
import com.fpt.booking.domain.enums.RequestTicketsStatus;
import com.fpt.booking.domain.payload.request.MotoRequest;
import com.fpt.booking.domain.payload.request.PaymentRequest;
import com.fpt.booking.domain.payload.request.RequestTicketDTO;
import com.fpt.booking.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;

    @GetMapping("/find-mechanic-in-radius")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Find mechanic in radius 5km")
    public ResponseEntity<?> findMechanicInRadius(@RequestParam(required = false) Double latitude,
                                                  @RequestParam(required = false) Double longitude,
                                                  @RequestParam String type){
        return createSuccessResponse("Khoảng cách từ xe tới mechanic", userService.findMechanicInRadius(latitude, longitude, type));
    }

    @GetMapping("/get-all-request-ticket")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get all request ticket")
    public ResponseEntity<?> getAllRequestTicket(@RequestParam(required = false) RequestTicketsStatus status,
                                                 @RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        return createSuccessResponse("Danh sách của phiếu yêu cầu", userService.getAllRequestTicket(status, pageNo, pageSize));
    }

    @PostMapping("/add-new-moto")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create a new moto for customer")
    public ResponseEntity<?> createANewMoto(@RequestBody MotoRequest motoRequest){
        return createSuccessResponse("Tạo xe mới", userService.createNewMoto(motoRequest));
    }

    @PostMapping("/upgrade-info-moto")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Upgrade information moto of customer")
    public ResponseEntity<?> updateInfoMoto(@RequestBody MotoRequest motoRequest) {
        return createSuccessResponse("Cập nhập thông tin xe", userService.updateInfoOfMoto(motoRequest));
    }

    @DeleteMapping("/delete-info-moto")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete information moto of customer")
    public ResponseEntity<?> deleteInfoMoto() {
        return createSuccessResponse("Cập nhập thông tin xe", userService.deleteInfoOfMoto());
    }

    @PostMapping("/create-new-appointment")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create new appointment of customer")
    public ResponseEntity<?> createNewAppointment(@RequestBody RequestTicketDTO requestTicketDTO){
        return createSuccessResponse("Tạo lịch hẹn mới.", userService.createNewAppointment(requestTicketDTO));
    }

    @PostMapping("/{id}/check-in")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Check in at garage of mechanic")
    public ResponseEntity<?> checkInAtGarageOfMechanic(@PathVariable Long id){
        return createSuccessResponse("Checkin tại garage", userService.checkIn(id));

    }

    @PostMapping("/{id}/approve-price")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Approve price of garage")
    public ResponseEntity<?> approvePriceOfGarage(@PathVariable Long id){
        return createSuccessResponse("Xác nhận báo giá xe", userService.approvePriceOfMechanic(id));
    }

    @PostMapping("/{id}/payment")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Payment for garage")
    public ResponseEntity<?> paymentForGarage(@PathVariable Long id, @RequestBody PaymentRequest paymentRequest){
        return createSuccessResponse("Thanh toán", userService.paymentForGarage(id, paymentRequest));
    }

    @PostMapping("/{id}/completed")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Completed of request ticket")
    public ResponseEntity<?> completedRequestTicket(@PathVariable Long id){
        return createSuccessResponse("Thanh toán", userService.completedRequestTicket(id));
    }

    @PostMapping("/{id}/canceled")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Canceled of request ticket")
    public ResponseEntity<?> canceledOfRequestTicket(@PathVariable Long id) {
        return createSuccessResponse("Hủy phiếu yêu cầu", userService.canceledRequestTicket(id));
    }

    @PostMapping("/request-upgrade-become-mechanic")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Request upgrade become mechanic")
    public ResponseEntity<?> requestUpgradeBecomeMechanic() {
        return createSuccessResponse("Tạo yêu cầu muốn trở thành mechanic", userService.requestUpgradeBecomeMechanic());
    }


}
