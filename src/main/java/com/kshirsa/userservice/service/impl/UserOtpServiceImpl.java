package com.kshirsa.userservice.service.impl;

import com.kshirsa.coreservice.BaseConstants;
import com.kshirsa.coreservice.exception.CustomException;
import com.kshirsa.coreservice.exception.ErrorCode;
import com.kshirsa.notificationservice.EmailValidationType;
import com.kshirsa.userservice.UserConstants;
import com.kshirsa.userservice.dto.response.GenerateOtpResponse;
import com.kshirsa.userservice.entity.OtpDetails;
import com.kshirsa.userservice.externalservice.checkmail.CheckMailConstants;
import com.kshirsa.userservice.externalservice.checkmail.CheckMailResponse;
import com.kshirsa.userservice.repository.OtpDetailsRepository;
import com.kshirsa.userservice.repository.UserDetailsRepository;
import com.kshirsa.notificationservice.EmailService;
import com.kshirsa.userservice.service.declaration.UserOtpService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserOtpServiceImpl implements UserOtpService {

    @Value("${checkMail.key}")
    private String checkMailKey;
    @Value("${checkMail.host}")
    private String checkMailHost;

    private final RestClient restClient;
    private final UserDetailsRepository userRepository;
    private final OtpDetailsRepository otpDetailsRepository;
    private final EmailService emailService;

    @Override
    public Boolean validateEmail(String email) throws CustomException {
        CheckMailResponse res = restClient
                .get()
                .uri(CheckMailConstants.CHECK_MAIL_URI + email)
                .header(CheckMailConstants.CHECK_MAIL_API_KEY, checkMailKey)
                .header(CheckMailConstants.CHECK_MAIL_HOST, checkMailHost)
                .retrieve().toEntity(CheckMailResponse.class).getBody();
        if (res != null) {
            if (res.isValid() && !res.isDisposable())
                return true;
            else if (res.isDisposable())
                throw new CustomException(ErrorCode.DISPOSABLE_EMAIL.name());
            else
                throw new CustomException(ErrorCode.INVALID_EMAIL_DOMAIN.name());
        }
        return false;
    }

    @Override
    public GenerateOtpResponse generateOtp(String email) throws MessagingException, CustomException, UnsupportedEncodingException {
        if (BaseConstants.ADMIN_EMAILS.contains(email)) {
            otpDetailsRepository
                    .save(new OtpDetails(email.toLowerCase(), 8520, LocalDateTime.now().plusMinutes(UserConstants.OTP_VALIDITY)));
            return new GenerateOtpResponse(true, email);
        }

        if (validateEmail(email)) {
            int otp = new Random().nextInt(1001, 10000);
            otpDetailsRepository
                    .save(new OtpDetails(email.toLowerCase(), otp, LocalDateTime.now().plusMinutes(UserConstants.OTP_VALIDITY)));
            if (userRepository.existsByUserEmail(email)) {
                emailService.sendOtpByEmail(EmailValidationType.LOGIN, email, Integer.toString(otp));
                return new GenerateOtpResponse(true, email);
            }
            emailService.sendOtpByEmail(EmailValidationType.SIGN_UP, email, Integer.toString(otp));
            return new GenerateOtpResponse(false, email);
        }
        return null;
    }

    @Override
    public Boolean validateOtp(String email, int otp) {
        OtpDetails details = otpDetailsRepository.findById(email.toLowerCase()).orElse(null);
        if (details != null) {
            if (details.getExpirationTime().isAfter(LocalDateTime.now())
                    && otp == details.getOtp()) {
                otpDetailsRepository.deleteById(details.getEmail());
                return true;
            }
        }
        return false;
    }
}
