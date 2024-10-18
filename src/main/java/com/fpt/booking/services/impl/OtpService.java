package com.fpt.booking.services.impl;


import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpService {
    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;

    private final TwilioRestClient twilioRestClient;

    public OtpService(@Value("${twilio.account.sid}") String accountSid,
                      @Value("${twilio.auth.token}") String authToken) {
        twilioRestClient = new TwilioRestClient.Builder(accountSid, authToken).build();
    }

    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void sendOtp(String recipientPhoneNumber, String otpCode) {
        Message.creator(new PhoneNumber(recipientPhoneNumber), new PhoneNumber(twilioPhoneNumber),
                        "Mã OTP của bạn là: " + otpCode)
                .create(twilioRestClient);
    }
}