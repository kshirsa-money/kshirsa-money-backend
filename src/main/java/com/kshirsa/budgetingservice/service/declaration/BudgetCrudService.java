package com.kshirsa.budgetingservice.service.declaration;

import com.kshirsa.budgetingservice.dto.AddBudgetSegmentDto;
import com.kshirsa.budgetingservice.dto.UpdateBudgetDto;
import com.kshirsa.budgetingservice.entity.BudgetSegment;

public interface BudgetCrudService {
    BudgetSegment addBudgetSegment(AddBudgetSegmentDto addBudgetSegmentDto);

    BudgetSegment getBudgetSegment(String segmentId);

    BudgetSegment updateBudgetSegment(UpdateBudgetDto updateSegmentDto);

    Boolean deleteBudgetSegment(String segmentId);
}
