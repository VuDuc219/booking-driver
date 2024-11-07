package com.fpt.booking.controller;

import com.fpt.booking.controller.base.BaseController;
import com.fpt.booking.domain.payload.request.VoucherRequest;
import com.fpt.booking.services.AdminService;
import com.fpt.booking.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController extends BaseController {

    private final AdminService adminService;

    private final AuthService authService;

    @GetMapping("/voucher")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get page of voucher")
    public ResponseEntity<?> getPageOfVoucher(@RequestParam Integer pageNo,
                                              @RequestParam Integer pageSize) {
        return createSuccessResponse("Get page of voucher", adminService.getVoucherResponses(pageNo, pageSize));
    }

    @GetMapping("/sum-of-revenue")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Sum of revenue")
    public ResponseEntity<?> sumOfRevenue() {
        return createSuccessResponse("Sum of revenue", adminService.sumOfRevenue());
    }

    @GetMapping("/sum-revenue-of-mechanic")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Sum of revenue")
    public ResponseEntity<?> sumMechanic() {
        return createSuccessResponse("Sum of revenue", adminService.sumRevenueOfMechanic());
    }

    @GetMapping("/get-all-review-application")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get page of review application")
    public ResponseEntity<?> getPageOfReviewApplication(@RequestParam Integer pageNo,
                                                        @RequestParam Integer pageSize){
        return createSuccessResponse("Get page of review application", adminService.getPageOfReviewApplication(pageNo, pageSize));
    }


    @GetMapping("/get-all-user")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get page of review application")
    public ResponseEntity<?> getPageOfUser(@RequestParam Integer pageNo,
                                                        @RequestParam Integer pageSize){
        return createSuccessResponse("Get all account", adminService.getPageAll(pageNo, pageSize));
    }

    @GetMapping("/voucher/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get page of voucher")
    public ResponseEntity<?> getPageOfVoucher(@PathVariable Long id) {
        return createSuccessResponse("Get page of voucher", adminService.getVoucherById(id));
    }

    @PostMapping("/voucher/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update voucher")
    public ResponseEntity<?> updateVoucher(@PathVariable Long id, @RequestBody VoucherRequest voucherRequest) {
        return createSuccessResponse("Update voucher", adminService.updateVoucher(id, voucherRequest));
    }


    @PostMapping("/voucher")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create voucher")
    public ResponseEntity<?> updateVoucher(@RequestBody VoucherRequest voucherRequest) {
        return createSuccessResponse("Save voucher", adminService.createVoucher(voucherRequest));
    }

    @DeleteMapping("/voucher/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create voucher")
    public ResponseEntity<?> deleteVoucher(@PathVariable Long id) {
        return createSuccessResponse("Save voucher", adminService.deleteVoucher(id));
    }

    @PostMapping("/{userId}/upgrade-mechanic-to-customer")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Upgrade mechanic to customer ")
    public ResponseEntity<?> upgradeMechanicToCustomer(@PathVariable Long userId) {
        return createSuccessResponse("Nâng cấp quyền của khách hàng", authService.upgradeMechanicToCustomer(userId));
    }


}
