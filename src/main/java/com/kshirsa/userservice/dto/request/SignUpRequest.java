package com.kshirsa.userservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kshirsa.coreservice.BaseConstants;
import com.kshirsa.userservice.UserConstants;
import com.kshirsa.userservice.entity.Gender;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record SignUpRequest(String name,
                            @Pattern(regexp=UserConstants.PHONE_NUMBER_REGEX, message = "INVALID_MOBILE") String phoneNumber,
                            String countryCode,
                            String country,
                            Gender gender,
                            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BaseConstants.DATE_FORMAT) LocalDate dob) {
}
