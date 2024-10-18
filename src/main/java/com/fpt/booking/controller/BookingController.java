package com.fpt.booking.controller;

import com.fpt.booking.controller.base.BaseController;
import com.fpt.booking.services.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController extends BaseController {

    private final BookingService bookingService;

    @GetMapping("/{id}/request-ticket")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get information of request ticket")
    public ResponseEntity<?> getInfoRequestTicket(@PathVariable Long id){
        return createSuccessResponse("Information of request ticket", bookingService.getInfoRequestTicket(id));
    }
}
