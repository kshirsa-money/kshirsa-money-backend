package com.kshirsa.userservice.repository;

import com.kshirsa.userservice.entity.UserDetails;
import org.springframework.data.repository.ListCrudRepository;

public interface UserDetailsRepository extends ListCrudRepository<UserDetails, String> {
    Boolean existsByUserEmail(String email);

    UserDetails findByUserEmail(String email);
}
