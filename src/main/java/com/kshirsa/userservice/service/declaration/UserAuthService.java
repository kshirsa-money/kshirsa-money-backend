package com.kshirsa.userservice.service.declaration;

import com.kshirsa.coreservice.exception.CustomException;
import com.kshirsa.userservice.dto.request.OtpValidateRequest;
import com.kshirsa.userservice.dto.response.LoginResponse;
import com.kshirsa.userservice.dto.response.NewTokenResponse;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

public interface UserAuthService {
    LoginResponse otpValidateFlow(OtpValidateRequest request, String deviceId, HttpServletRequest httpRequest) throws CustomException, MessagingException, UnsupportedEncodingException;

    NewTokenResponse refreshToken(String token, String deviceId) throws CustomException;

    Boolean logout(String token);
}
