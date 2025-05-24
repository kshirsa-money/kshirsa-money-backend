package com.kshirsa.trackingservice.repository;

import com.kshirsa.trackingservice.entity.HashTags;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface HashTagRepo extends ListCrudRepository<HashTags, String> {

    @Query(value = "SELECT h.hash_tag FROM hash_tags h WHERE h.user_id = ?1", nativeQuery = true)
    Optional<Object> findHashTagsByUserId(String userId);
}
