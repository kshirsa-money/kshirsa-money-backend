package com.kshirsa.budgetingservice.service.declaration;

import com.kshirsa.budgetingservice.dto.AddBudgetSegmentDto;
import com.kshirsa.budgetingservice.entity.BudgetSegment;

public interface AddBudgetService {
    BudgetSegment addBudgetSegment(AddBudgetSegmentDto addBudgetSegmentDto);
}
