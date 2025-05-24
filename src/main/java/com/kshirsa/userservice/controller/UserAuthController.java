package com.kshirsa.userservice.controller;

import com.kshirsa.coreservice.BaseConstants;
import com.kshirsa.coreservice.BaseController;
import com.kshirsa.coreservice.SuccessResponse;
import com.kshirsa.coreservice.exception.CustomException;
import com.kshirsa.userservice.UserConstants;
import com.kshirsa.userservice.dto.request.OtpValidateRequest;
import com.kshirsa.userservice.dto.response.GenerateOtpResponse;
import com.kshirsa.userservice.dto.response.LoginResponse;
import com.kshirsa.userservice.dto.response.NewTokenResponse;
import com.kshirsa.userservice.externalservice.geolite.GeoLite2Service;
import com.kshirsa.userservice.externalservice.geolite.LocationFromIpResponse;
import com.kshirsa.userservice.service.declaration.UserAuthService;
import com.kshirsa.userservice.service.declaration.UserOtpService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Enumeration;

@RestController
@RequestMapping(path = BaseConstants.ROOT_PATH + "/auth", produces = "application/json")
@RequiredArgsConstructor
@Validated
@Tag(name = "1. User Public Controller", description = "Public endpoints for user signup, login, email validation")
public class UserAuthController extends BaseController {

    private final UserOtpService userOtpService;
    private final UserAuthService userAuthService;
    private final Environment env;
    private final GeoLite2Service geoLite2Service;

    @Operation(summary = "Generate OTP for email validation for sign up & login")
    @GetMapping(path = "/otp")
    public ResponseEntity<SuccessResponse<GenerateOtpResponse>> generateEmailOtp(@Pattern(regexp= UserConstants.EMAIL_REGEX, message = "INVALID_EMAIL_FORMAT")
                                                                                     @NotNull String email) throws CustomException, MessagingException, UnsupportedEncodingException {
        return ok(userOtpService.generateOtp(email), env.getProperty("OTP.SENT") + email);
    }

    @Operation(summary = "Validate OTP for login & get access tokens in response")
    @PostMapping("/otp/validate")
    public ResponseEntity<SuccessResponse<LoginResponse>> otpValidate(@RequestHeader(value = UserConstants.DEVICE_ID)@NonNull String deviceId,
                                                                      @RequestBody @Valid OtpValidateRequest request,
                                                                      HttpServletRequest httpRequest) throws CustomException, MessagingException, UnsupportedEncodingException {
        return ok(userAuthService.otpValidateFlow(request,deviceId, httpRequest), env.getProperty("USER.LOGGEDIN"));
    }

    @Operation(summary = "Get new JWT token from refresh token")
    @GetMapping(path = "/refresh")
    public ResponseEntity<SuccessResponse<NewTokenResponse>> refreshToken(@RequestHeader(value = UserConstants.DEVICE_ID)@NonNull String deviceId,
                                                                          @NonNull String token) throws CustomException {
        return ok(userAuthService.refreshToken(token,deviceId), env.getProperty("NEW.TOKEN.GENERATED"));
    }

    @Operation(summary = "Logout user and delete Refresh Token")
    @PostMapping(path = "/logout")
    public ResponseEntity<SuccessResponse<Boolean>> logout(@NonNull String token) {
        return ok(userAuthService.logout(token), env.getProperty("NEW.TOKEN.GENERATED"));
    }

    @Operation(summary = "Unauthenticated endpoint to check if the service is up and running.")
    @GetMapping("/index")
    public String index() {
        return BaseConstants.INDEX_RESPONSE;
    }


//    -----------------------------------------------------------------------------------------------------------------

    @Hidden
    @GetMapping("/getIp")
    public LocationFromIpResponse test(HttpServletRequest request) {
        return geoLite2Service.getClientLocation(request);
    }

    @Hidden
    @GetMapping("/print-all-headers")
    public String printAllHeaders(HttpServletRequest request) {
        StringBuilder headersInfo = new StringBuilder("All Headers and Their Values:\n");

        // Get all header names
        Enumeration<String> headerNames = request.getHeaderNames();

        // Iterate through the header names and fetch their corresponding values
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            // Get all values for the current header name (in case of multiple values)
            java.util.List<String> headerValues = Collections.list(request.getHeaders(headerName));

            headersInfo.append(headerName).append(": ").append(String.join(", ", headerValues)).append("\n");
        }

        return headersInfo.toString();
    }
}
