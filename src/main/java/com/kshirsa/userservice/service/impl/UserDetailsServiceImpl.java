package com.kshirsa.userservice.service.impl;

import com.kshirsa.coreservice.exception.CustomException;
import com.kshirsa.coreservice.exception.ErrorCode;
import com.kshirsa.userservice.dto.request.SignUpRequest;
import com.kshirsa.userservice.dto.response.UserResponse;
import com.kshirsa.userservice.entity.UserDetails;
import com.kshirsa.userservice.repository.UserDetailsRepository;
import com.kshirsa.userservice.service.declaration.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDetailsRepository userRepository;

    @Override
    public String getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Override
    public UserResponse updateUserDetails(SignUpRequest request) throws CustomException {
        UserDetails user = userRepository.findById(getUser()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND.name()));
        user.setName(request.name());
        user.setPhoneNumber(request.phoneNumber());
        user.setCountryCode(request.countryCode());
        user.setCountry(request.country());
        user.setGender(request.gender());
        user.setDob(request.dob());
        user = userRepository.save(user);
        return new UserResponse(user, isSignupFlowCompleted(user));
    }

    @Override
    public UserResponse getUserDetails() throws CustomException {
        UserDetails user = userRepository.findById(getUser()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND.name()));
        return new UserResponse(user, isSignupFlowCompleted(user));
    }

    public Boolean isSignupFlowCompleted(UserDetails user) {
        return user != null && user.getName() != null
                && user.getPhoneNumber() != null
                && user.getCountryCode() != null
                && user.getGender() != null
                && user.getDob() != null;
    }
}
