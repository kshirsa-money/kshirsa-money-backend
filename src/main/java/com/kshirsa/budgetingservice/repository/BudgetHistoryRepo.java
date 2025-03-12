package com.kshirsa.budgetingservice.repository;

import com.kshirsa.budgetingservice.entity.BudgetHistory;
import org.springframework.data.repository.ListCrudRepository;

public interface BudgetHistoryRepo extends ListCrudRepository<BudgetHistory, Integer> {
}
