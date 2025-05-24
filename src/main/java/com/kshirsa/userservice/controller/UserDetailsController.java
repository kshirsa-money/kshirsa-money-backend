package com.kshirsa.userservice.controller;

import com.kshirsa.coreservice.BaseConstants;
import com.kshirsa.coreservice.BaseController;
import com.kshirsa.coreservice.SuccessResponse;
import com.kshirsa.coreservice.exception.CustomException;
import com.kshirsa.userservice.dto.request.SignUpRequest;
import com.kshirsa.userservice.dto.response.UserResponse;
import com.kshirsa.userservice.service.declaration.UserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = BaseConstants.ROOT_PATH + "/my", produces = "application/json")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "2. User Details Controller", description = "Authenticated endpoints for user details")

public class UserDetailsController extends BaseController {

    private final UserDetailsService userDetailsService;
    private final Environment env;

    @Operation(summary = "Update user Details")
    @PutMapping("/update/details")
    public ResponseEntity<SuccessResponse<UserResponse>> otpValidate(@RequestBody SignUpRequest request) throws CustomException {
        return ok(userDetailsService.updateUserDetails(request), env.getProperty("USER.DATA.UPDATED"));
    }

    @Operation(summary = "Get user Details")
    @GetMapping("/get/details")
    public ResponseEntity<SuccessResponse<UserResponse>> getDetails() throws CustomException {
        return ok(userDetailsService.getUserDetails(), env.getProperty("USER.DATA.RETRIEVED"));
    }

//    @Operation(summary = "Send otp for email update")

    @Operation(summary = "Authenticated endpoint to check if authentication is working.")
    @GetMapping("/index")
    public String index() {
        return BaseConstants.INDEX_RESPONSE;
    }
}
