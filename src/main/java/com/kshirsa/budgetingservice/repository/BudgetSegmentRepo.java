package com.kshirsa.budgetingservice.repository;

import com.kshirsa.budgetingservice.entity.BudgetSegment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface BudgetSegmentRepo extends ListCrudRepository<BudgetSegment, String> {

    List<BudgetSegment> findAllByParentSegmentId(String segmentId);

    List<BudgetSegment> findAllByUserId(String userId);

    @Query(value = " SELECT * FROM budget_segment WHERE transaction_categories = ?1 AND user_id = ?2 ", nativeQuery = true)
    List<BudgetSegment> findAllByCategoryId(String categoryId, String userId);
}
