package com.kshirsa.trackingservice.repository;

import com.kshirsa.trackingservice.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Set;

public interface CategoryRepo extends ListCrudRepository<Category,String> {

    @Query(value =
            """
            SELECT * FROM category c WHERE (c.created_by = ?1 OR c.created_by = 'SYSTEM')
            AND ( ?2 IS NULL OR c.transaction_type = ?2 )
            """, nativeQuery = true)
    List<Category> getAllCategory(String userId, String transactionType);

    @Query(value =" SELECT c.category_name FROM category c WHERE c.created_by = ?1 OR c.created_by = 'SYSTEM' ", nativeQuery = true)
    Set<String> getCategoryByUserId(String userId);
}
