package com.kshirsa.budgetingservice.repository;

import com.kshirsa.budgetingservice.entity.BudgetSegment;
import org.springframework.data.repository.ListCrudRepository;

public interface BudgetSegmentRepo extends ListCrudRepository<BudgetSegment, Integer> {
}
