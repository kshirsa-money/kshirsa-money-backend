package com.kshirsa.budgetingservice.service.impl;

import com.kshirsa.budgetingservice.dto.AddBudgetSegmentDto;
import com.kshirsa.budgetingservice.entity.BudgetSegment;
import com.kshirsa.budgetingservice.repository.BudgetSegmentRepo;
import com.kshirsa.budgetingservice.service.declaration.AddBudgetService;
import com.kshirsa.userservice.service.declaration.UserDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AddBudgetServiceImpl implements AddBudgetService {

    private final BudgetSegmentRepo budgetSegmentRepo;
    private final UserDetailsService userDetailsService;

    @Override
    public BudgetSegment addBudgetSegment(AddBudgetSegmentDto addBudgetSegmentDto) {
        BudgetSegment budgetSegment = BudgetSegment.budgetSegmentDtoToEntity(addBudgetSegmentDto);
        budgetSegment.setUserId(userDetailsService.getUser());
         return budgetSegmentRepo.save(budgetSegment);
    }
}
