package com.fpt.booking.services.impl;

import com.fpt.booking.common.CommonMapper;
import com.fpt.booking.common.Constant;
import com.fpt.booking.config.ResourceBundleConfig;
import com.fpt.booking.domain.entities.FirebaseDevice;
import com.fpt.booking.domain.entities.Role;
import com.fpt.booking.domain.entities.User;
import com.fpt.booking.domain.enums.AuthProvider;
import com.fpt.booking.domain.enums.RoleName;
import com.fpt.booking.domain.payload.request.*;
import com.fpt.booking.domain.payload.response.AuthResponse;
import com.fpt.booking.domain.payload.response.MessageResponse;
import com.fpt.booking.domain.payload.response.UserResponse;
import com.fpt.booking.domain.payload.response.VerifyOTPResponse;
import com.fpt.booking.exception.BadRequestException;
import com.fpt.booking.repository.FirebaseDeviceRepository;
import com.fpt.booking.repository.RoleRepository;
import com.fpt.booking.repository.UserRepository;
import com.fpt.booking.secruity.TokenProvider;
import com.fpt.booking.services.AuthService;
import com.fpt.booking.services.FileUploadService;
import com.fpt.booking.utils.MessageUtils;
import com.fpt.booking.utils.Regex;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class IAuthService extends BaseService implements AuthService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;

    private final ResourceBundleConfig resourceBundleConfig;

    private final TokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder;

    private final CommonMapper commonMapper;

    private final FileUploadService fileUploadService;

    private final JavaMailSender mailSender;

    private final FirebaseDeviceRepository firebaseDeviceRepository;
    private final Path rootDir = Paths.get("email-templates");

    @Override
    public MessageResponse registerAccount(SignUpRequest signUpRequest) throws MessagingException, IOException {

        if (isValidPassword(signUpRequest.getPassword())) {
            throw new BadRequestException(this.resourceBundleConfig.getViMessage(MessageUtils.PASSWORD_NOT_VALIDATE));
        }
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        User user = new User(
                signUpRequest.getName(),
                signUpRequest.getPhone(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                AuthProvider.local,
                false,
                false);
        if (RoleName.ROLE_USER.equals(signUpRequest.getRole())) {
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new IllegalArgumentException(this.resourceBundleConfig.getViMessage(MessageUtils.ROLE_EMPTY)));

            user.setRoles(Collections.singleton(userRole));
            user.setOtp(otp);
            userRepository.save(user);

        } else if (RoleName.ROLE_MECHANIC.equals(signUpRequest.getRole())) {
            Role userRole = roleRepository.findByName(RoleName.ROLE_MECHANIC)
                    .orElseThrow(() -> new IllegalArgumentException(this.resourceBundleConfig.getViMessage(MessageUtils.ROLE_EMPTY)));
            user.setRoles(Collections.singleton(userRole));
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException(this.resourceBundleConfig.getViMessage(MessageUtils.NOT_CREATED_ACCOUNT));
        }
        sendOTPEmailFromTemplate("REGISTER", signUpRequest.getEmail(), String.valueOf(otp), user.getName());
        return MessageResponse.builder().message("Đăng kí tài khoản thành công với email: " + signUpRequest.getEmail()).build();
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication);
        Long userId = tokenProvider.getUserIdFromToken(jwt);
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException(this.resourceBundleConfig.getViMessage(MessageUtils.USER_NOT_FOUND)));
        saveUserDevice(user, loginRequest);
        Set<Role> roles = user.getRoles();

        for (Role role : roles) {
            if (RoleName.ROLE_ADMIN.equals(role.getName())) {
                return new AuthResponse(jwt, 3);
            } else if (RoleName.ROLE_USER.equals(role.getName())) {
                return new AuthResponse(jwt, 1);
            } else if (RoleName.ROLE_MECHANIC.equals(role.getName())) {
                return new AuthResponse(jwt, 2);
            } else {
                throw new BadRequestException(this.resourceBundleConfig.getViMessage(MessageUtils.ROLE_EMPTY));
            }
        }
        return new AuthResponse(jwt);
    }

    @Override
    public UserResponse getInformationOfAccount() {
        return getAccountResponse();
    }

    @Override
    public MessageResponse uploadImgForInfo(MultipartFile file) {
        User user = getAccountById();
        if (Objects.nonNull(file)) {
            fileUploadService.save(file);
            user.setImageUrl(Constant.URL_IMAGE + file.getOriginalFilename());
        }
        userRepository.save(user);
        return MessageResponse.builder().message(this.resourceBundleConfig.getViMessage(MessageUtils.CHANGE_NEW_IMAGE)).build();
    }

    @Override
    public MessageResponse forgotPassword(EmailRequest emailRequest) throws MessagingException, IOException {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        User user = userRepository.findByEmail(emailRequest.getEmail()).orElseThrow(() -> new BadRequestException(this.resourceBundleConfig.getViMessage(MessageUtils.ACCOUNT_NOT_FOUND)));
        user.setOtp(otp);
        userRepository.save(user);
        sendOTPEmailFromTemplate("RESET", emailRequest.getEmail(), String.valueOf(otp), user.getName());
        return MessageResponse.builder().message(this.resourceBundleConfig.getViMessage(MessageUtils.SEND_OTP_EMAIL)).build();
    }

    @Override
    public MessageResponse resetPassword(ResetPasswordRequest resetPasswordRequest) {
        User user = userRepository.findByOtp(resetPasswordRequest.getOtp()).orElseThrow(() -> new BadRequestException(this.resourceBundleConfig.getViMessage(MessageUtils.ACCOUNT_NOT_FOUND)));
        if (isValidPassword(resetPasswordRequest.getNewPassword())) {
            throw new BadRequestException(this.resourceBundleConfig.getViMessage(MessageUtils.PASSWORD_NOT_VALIDATE));
        }

        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmedPassword())) {
            throw new BadRequestException(this.resourceBundleConfig.getViMessage(MessageUtils.PASSWORD_NOT_CONFIRMED));
        }
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        userRepository.save(user);
        return MessageResponse.builder().message(this.resourceBundleConfig.getViMessage(MessageUtils.CHANGE_PASSWORD)).build();
    }

    @Override
    public VerifyOTPResponse verifyOTP(VerifyOTP verifyOTP) {
        User user = userRepository.findByEmail(verifyOTP.getEmail()).orElseThrow(() -> new BadRequestException(this.resourceBundleConfig.getViMessage(MessageUtils.ACCOUNT_NOT_FOUND)));
        if (user.getOtp().equals(verifyOTP.getOtp())) {
            if (verifyOTP.getType().equals("REGISTER")) {
                user.setIsActive(Boolean.TRUE);
            }
            userRepository.save(user);
            return VerifyOTPResponse.builder().otp(verifyOTP.getOtp()).build();
        } else {
            throw new BadRequestException(this.resourceBundleConfig.getViMessage(MessageUtils.OTP_NOT_MATCH));
        }
    }

    @Override
    public MessageResponse upgradeInfo(UserRequest userRequest) {
        User user = getAccountById();
        user.setEmail(userRequest.getEmail());
        user.setName(userRequest.getName());
        user.setAddress(userRequest.getAddress());
        userRepository.save(user);
        return MessageResponse.builder().message(this.resourceBundleConfig.getViMessage(MessageUtils.UPGRADE_INFO)).build();
    }

    @Override
    public Page<UserResponse> getAllAccount(Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public MessageResponse upgradeMechanicToCustomer(Long userId) {
        userRepository.updateRoleOfCustomer(userId);
        User user = userRepository.findById(userId).orElseThrow();
        user.setIsMechanic(false);
        userRepository.save(user);
        return MessageResponse.builder().message("Cập nhật quyền thành công.").build();
    }

    private void saveUserDevice(User user, LoginRequest loginRequest) {
        List<FirebaseDevice> firebaseDevices = firebaseDeviceRepository.findByDeviceToken(loginRequest.getDeviceToken());

        if (firebaseDevices.isEmpty()) {
            FirebaseDevice firebaseDevice = new FirebaseDevice(user, loginRequest.getDeviceToken());
            firebaseDeviceRepository.save(firebaseDevice);
        } else {
            for (FirebaseDevice firebaseDevice : firebaseDevices) {
                firebaseDevice.setUser(user);
            }
            firebaseDeviceRepository.saveAll(firebaseDevices);
        }
    }

    private String getTokenOfUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.createToken(authentication);
    }

    public static boolean isValidVietnamesePhoneNumber(String phoneNumber) {
        return Pattern.matches(Regex.PHONE_VALIDATION, phoneNumber);
    }

    public static boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(Regex.PASSWORD_VALIDATION);
        Matcher matcher = pattern.matcher(password);
        return !matcher.matches();
    }

    private User getAccountById() {
        return userRepository.findById(getUserId()).orElseThrow(() -> new BadRequestException(this.resourceBundleConfig.getViMessage(MessageUtils.ACCOUNT_NOT_FOUND)));
    }

    private UserResponse getAccountResponse() {
        User user = getAccountById();
        Set<Role> roles = user.getRoles();

        UserResponse userResponse = commonMapper.convertToResponse(getAccountById(), UserResponse.class);

        for (Role role : roles) {
            if (RoleName.ROLE_ADMIN.equals(role.getName())) {
                userResponse.setRoleName(RoleName.ROLE_ADMIN);
            } else if (RoleName.ROLE_USER.equals(role.getName())) {
                userResponse.setRoleName(RoleName.ROLE_USER);
            } else if (RoleName.ROLE_MECHANIC.equals(role.getName())) {
                userResponse.setRoleName(RoleName.ROLE_MECHANIC);
            } else {
                throw new BadRequestException(this.resourceBundleConfig.getViMessage(MessageUtils.ROLE_EMPTY));
            }
        }
        return userResponse;
    }

    public void sendOTPEmailFromTemplate(String type, String email, String otp, String name) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress("admin@gmail.com"));
        message.setRecipients(MimeMessage.RecipientType.TO, email);
        if (type.equals("REGISTER")) {
            message.setSubject("Mã OTP - Xác thực tài khoản.");
        } else {
            message.setSubject("Mã OTP - Để có thể đổi mật khẩu.");
        }

        // Create a MimeMultipart object to hold the email content
        MimeMultipart multipart = new MimeMultipart();

        // Create a MimeBodyPart object to hold the email body
        MimeBodyPart bodyPart = new MimeBodyPart();

        // Set the email body content
        String body = "Xin chào " + name + ", mã OTP của bạn là: " + otp;
        bodyPart.setText(body, "UTF-8");

        // Add the email body to the MimeMultipart object
        multipart.addBodyPart(bodyPart);

        // Set the MimeMultipart object as the email content
        message.setContent(multipart);

        mailSender.send(message);
    }

    public String readFile(String filename) throws IOException {
        Path filePath = rootDir.resolve(filename);
        return Files.readString(filePath, StandardCharsets.UTF_8);
    }
}
