package com.kshirsa.userservice.service.declaration;

import com.kshirsa.coreservice.exception.CustomException;
import com.kshirsa.userservice.dto.response.GenerateOtpResponse;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface UserOtpService {

    Boolean validateEmail(String email) throws CustomException;

    GenerateOtpResponse generateOtp(String email) throws MessagingException, CustomException, UnsupportedEncodingException;

    Boolean validateOtp(String email, int otp);
}
