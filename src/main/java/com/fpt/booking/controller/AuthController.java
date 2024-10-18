package com.fpt.booking.controller;

import com.fpt.booking.common.Constant;
import com.fpt.booking.controller.base.BaseController;
import com.fpt.booking.controller.base.message.ExtendedMessage;
import com.fpt.booking.domain.payload.request.*;
import com.fpt.booking.domain.payload.response.UserResponse;
import com.fpt.booking.repository.UserRepository;
import com.fpt.booking.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new account ")
    @ApiResponse(responseCode = Constant.API_RESPONSE.API_STATUS_OK_STR, description = "Register a new account successful",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class))})
    @ApiResponse(responseCode = Constant.API_RESPONSE.API_STATUS_BAD_REQUEST_STR, description = "Input invalid",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class))})
    @ApiResponse(responseCode = Constant.API_RESPONSE.API_STATUS_INTERNAL_SERVER_ERROR_STR, description = "Internal Server Error",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class))})
    public ResponseEntity<?> registerNewAccount(@Valid @RequestBody SignUpRequest signUpRequest) throws MessagingException, IOException {
        return createSuccessResponse("Đăng kí tài khoản thành công", authService.registerAccount(signUpRequest));
    }


    @PostMapping("/login")
    @Operation(summary = "Login for account ")
    @ApiResponse(responseCode = Constant.API_RESPONSE.API_STATUS_OK_STR, description = "Login for account successful",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class))})
    @ApiResponse(responseCode = Constant.API_RESPONSE.API_STATUS_BAD_REQUEST_STR, description = "Input invalid",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class))})
    @ApiResponse(responseCode = Constant.API_RESPONSE.API_STATUS_INTERNAL_SERVER_ERROR_STR, description = "Internal Server Error",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class))})
    public ResponseEntity<?> loginForAccount(@Valid @RequestBody LoginRequest loginRequest) {
        return createSuccessResponse("Đăng nhập tài khoản thành công", authService.login(loginRequest));
    }

    @GetMapping("/info")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('USER','MECHANIC','ADMIN')")
    @Operation(summary = "Get information of account")
    @ApiResponse(responseCode = Constant.API_RESPONSE.API_STATUS_OK_STR, description = "Get information of account successful",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class))})
    @ApiResponse(responseCode = Constant.API_RESPONSE.API_STATUS_BAD_REQUEST_STR, description = "Input invalid",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class))})
    @ApiResponse(responseCode = Constant.API_RESPONSE.API_STATUS_INTERNAL_SERVER_ERROR_STR, description = "Internal Server Error",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class))})
    public ResponseEntity<?> getInfoOfAccount(){
        return createSuccessResponse("Thông tin cá nhân", authService.getInformationOfAccount());
    }

    @PostMapping("/upload-image-for-info")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('USER','MECHANIC','ADMIN')")
    @Operation(summary = "Change image for user")
    @ApiResponse(responseCode = Constant.API_RESPONSE.API_STATUS_OK_STR, description = "Change image for user successful",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class))})
    @ApiResponse(responseCode = Constant.API_RESPONSE.API_STATUS_BAD_REQUEST_STR, description = "Input invalid",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class))})
    @ApiResponse(responseCode = Constant.API_RESPONSE.API_STATUS_INTERNAL_SERVER_ERROR_STR, description = "Internal Server Error",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExtendedMessage.class))})
    public ResponseEntity<?> uploadImageForInfo(@RequestParam(required = false) MultipartFile file) {
        return createSuccessResponse("Cập nhật ảnh đại diện", authService.uploadImgForInfo(file));
    }

    @PostMapping("/upgrade-info")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('USER','MECHANIC')")
    @Operation(summary = "Upgrade info")
    public ResponseEntity<?> upgradeInfo(@Valid @RequestBody UserRequest userRequest) throws MessagingException, IOException {
        return createSuccessResponse(authService.upgradeInfo(userRequest));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody EmailRequest emailRequest) throws MessagingException, IOException {
        return createSuccessResponse(authService.forgotPassword(emailRequest));
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP")
    public ResponseEntity<?> verifyOTP(@Valid @RequestBody VerifyOTP verifyOTP) throws MessagingException, IOException {
        return createSuccessResponse(authService.verifyOTP(verifyOTP));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        return createSuccessResponse(authService.resetPassword(resetPasswordRequest));
    }

    @GetMapping("/get-all-acount")
    @Operation(summary = "Get all account")
    public ResponseEntity<?> getAllAccount(@RequestParam Integer pageNo,@RequestParam Integer pageSize){
        return createSuccessResponse(authService.getAllAccount(pageNo, pageSize));
    }
}
