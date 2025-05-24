package com.kshirsa.userservice.service.declaration;

import com.kshirsa.coreservice.exception.CustomException;
import com.kshirsa.userservice.dto.request.SignUpRequest;
import com.kshirsa.userservice.dto.response.UserResponse;

public interface UserDetailsService {
    String getUser();

    UserResponse updateUserDetails(SignUpRequest request) throws CustomException;

    UserResponse getUserDetails() throws CustomException;
}
