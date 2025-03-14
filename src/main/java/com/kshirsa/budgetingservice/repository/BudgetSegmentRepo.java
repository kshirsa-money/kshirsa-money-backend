package com.kshirsa.budgetingservice.repository;

import com.kshirsa.budgetingservice.entity.BudgetSegment;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface BudgetSegmentRepo extends ListCrudRepository<BudgetSegment, String> {

    List<BudgetSegment> findAllByParentSegmentId(String segmentId);
}
