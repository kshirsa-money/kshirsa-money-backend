package com.kshirsa.budgetingservice.service.impl;

import com.kshirsa.budgetingservice.entity.BudgetSegment;
import com.kshirsa.budgetingservice.repository.BudgetHistoryRepo;
import com.kshirsa.budgetingservice.repository.BudgetSegmentRepo;
import com.kshirsa.userservice.service.declaration.UserDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class BudgetProcessService {

    private final BudgetSegmentRepo budgetSegmentRepo;
    private final BudgetHistoryRepo budgetHistoryRepo;
    private final UserDetailsService userDetailsService;

    @Scheduled(cron = "0 0 0 1 * ?")
    public void archiveBudget() {
        String userId = userDetailsService.getUser();
        List<BudgetSegment> budgets = budgetSegmentRepo.findAllByUserId(userId);
        if (!budgets.isEmpty()) {
            budgetHistoryRepo.saveAll(budgets.stream().map(BudgetSegment::convertToBudgetHistory).toList());
        }
    }
}
