package com.kshirsa.userservice.service.impl;

import com.kshirsa.coreservice.BaseConstants;
import com.kshirsa.coreservice.exception.CustomException;
import com.kshirsa.coreservice.exception.ErrorCode;
import com.kshirsa.notificationservice.EmailService;
import com.kshirsa.userservice.dto.request.OtpValidateRequest;
import com.kshirsa.userservice.dto.response.LoginResponse;
import com.kshirsa.userservice.dto.response.NewTokenResponse;
import com.kshirsa.userservice.entity.RefreshToken;
import com.kshirsa.userservice.entity.UserDetails;
import com.kshirsa.userservice.externalservice.geolite.GeoLite2Service;
import com.kshirsa.userservice.repository.UserDetailsRepository;
import com.kshirsa.userservice.security.JwtHelper;
import com.kshirsa.userservice.service.RefreshTokenService;
import com.kshirsa.userservice.service.declaration.UserAuthService;
import com.kshirsa.userservice.service.declaration.UserOtpService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final JwtHelper jwtHelper;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsRepository userRepository;
    private final UserOtpService userOtpService;
    private final EmailService emailService;
    private final GeoLite2Service geoLite2Service;

    @Override
    public LoginResponse otpValidateFlow(OtpValidateRequest request, String deviceId, HttpServletRequest httpRequest) throws CustomException, MessagingException, UnsupportedEncodingException {
        if (userOtpService.validateOtp(request.email(), request.otp())) {           //Validating OTP
            UserDetails user = userRepository.findByUserEmail(request.email());
            if (user == null) {                                                     //If user is registered or not
                user = userRepository.save(new UserDetails(request.email(), LocalDateTime.now(), deviceId));      //Creating new user

                if (!BaseConstants.ADMIN_EMAILS.contains(request.email()))
                    emailService.sendWelcomeMail(user.getUserEmail());              //Sending welcome mail to user

                RefreshToken token = refreshTokenService.getRefreshToken(user.getUserId(), deviceId);
                return new LoginResponse(jwtHelper.generateToken(user.getUserId()),
                        token.getToken(), token.getExpirationDate(),
                        false,
                        user);
            } else {                                                                //If user is already registered
                RefreshToken token = refreshTokenService.getRefreshToken(user.getUserId(), deviceId);

                if(!user.getLoggedInDevices().contains(deviceId) ) {                //Checking if user is logging in from new device
                    user.getLoggedInDevices().add(deviceId);
                    userRepository.save(user);

                    if (!BaseConstants.ADMIN_EMAILS.contains(request.email()))
                        emailService.sendNewDeviceLoginEmail(user.getUserEmail(), geoLite2Service.getClientLocation(httpRequest));
                                                                                    //Sending new device login mail to user

                }
                return new LoginResponse(jwtHelper.generateToken(user.getUserId()),
                        token.getToken(), token.getExpirationDate(),
                        isSignupFlowCompleted(user),
                        user);
            }
        } else
            throw new CustomException(ErrorCode.INVALID_OTP.name());
    }

    @Override
    public NewTokenResponse refreshToken(String token, String deviceId) throws CustomException {
        String userid = refreshTokenService.tokenValidation(token,deviceId);
        return new NewTokenResponse(jwtHelper.generateToken(userid),
                refreshTokenService.extendTokenTime(token));
    }

    @Override
    public Boolean logout(String token) {
        refreshTokenService.deleteToken(token);
        return true;
    }

    public Boolean isSignupFlowCompleted(UserDetails user) {
        return user != null && user.getName() != null
                && user.getPhoneNumber() != null
                && user.getCountryCode() != null
                && user.getGender() != null
                && user.getDob() != null;
    }

}
