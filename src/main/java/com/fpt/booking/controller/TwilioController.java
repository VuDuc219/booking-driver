package com.fpt.booking.controller;

import com.fpt.booking.services.impl.OtpService;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class TwilioController {

    private final OtpService otpService;


    @PostMapping("/sendOtp")
    public ResponseEntity<String> sendOtp(@RequestParam String phoneNumber) {
        String otpCode = otpService.generateOtp();
        otpService.sendOtp(phoneNumber, otpCode);
        return ResponseEntity.ok("OTP sent successfully.");
    }
}