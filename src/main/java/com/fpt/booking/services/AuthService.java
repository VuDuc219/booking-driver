package com.fpt.booking.services;

import com.fpt.booking.domain.payload.request.*;
import com.fpt.booking.domain.payload.response.AuthResponse;
import com.fpt.booking.domain.payload.response.MessageResponse;
import com.fpt.booking.domain.payload.response.UserResponse;
import com.fpt.booking.domain.payload.response.VerifyOTPResponse;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AuthService {
    MessageResponse registerAccount(SignUpRequest signUpRequest) throws MessagingException, IOException;

    AuthResponse login(LoginRequest loginRequest);

    UserResponse getInformationOfAccount();

    MessageResponse uploadImgForInfo(MultipartFile file);

    MessageResponse forgotPassword(EmailRequest emailRequest) throws MessagingException, IOException;

    MessageResponse resetPassword(ResetPasswordRequest resetPasswordRequest);

    VerifyOTPResponse verifyOTP(VerifyOTP verifyOTP);

    MessageResponse upgradeInfo(UserRequest userRequest);

    Page<UserResponse> getAllAccount(Integer pageNo, Integer pageSize);

    MessageResponse upgradeMechanicToCustomer(Long userId);
}
