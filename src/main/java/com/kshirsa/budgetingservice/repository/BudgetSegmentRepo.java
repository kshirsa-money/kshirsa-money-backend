package com.kshirsa.budgetingservice.repository;

import com.kshirsa.budgetingservice.entity.BudgetSegment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface BudgetSegmentRepo extends ListCrudRepository<BudgetSegment, String> {

    List<BudgetSegment> findAllByParentSegmentId(String segmentId);

    List<BudgetSegment> findAllByUserId(String userId);

    @Query(value = """
            SELECT * FROM budget_segment b
            INNER JOIN segment_category s ON b.segment_id = s.join_segment_id
            WHERE s.join_category_id = ?1 AND b.user_id = ?2
            """, nativeQuery = true)
    List<BudgetSegment> findAllByCategoryId(String categoryId, String userId);
}
