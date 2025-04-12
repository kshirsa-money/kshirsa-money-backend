package com.kshirsa.budgetingservice.service.impl;

import com.kshirsa.budgetingservice.entity.BudgetSegment;
import com.kshirsa.budgetingservice.repository.BudgetHistoryRepo;
import com.kshirsa.budgetingservice.repository.BudgetSegmentRepo;
import com.kshirsa.trackingservice.entity.Category;
import com.kshirsa.userservice.service.declaration.UserDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
            budgetSegmentRepo.saveAll(budgets.stream().peek(budget -> budget.setSpentAmount(0.0)).toList());
        }
    }

    @Async
    public void initializeBudget(BudgetSegment budgetSegment) {
        Double totalSpent = budgetSegment.getTransactionCategories().stream()
                .mapToDouble(Category::getTotalSpent)
                .sum();
        budgetSegment.setSpentAmount(totalSpent);
        budgetSegmentRepo.save(budgetSegment);
    }

    @Async
    public void updateBudget(String categoryId, Double amount) {

        String userId = userDetailsService.getUser();

        List<BudgetSegment> startSegments = budgetSegmentRepo.findAllByCategoryId(categoryId, userId);              // Get all segments associated with this category

        if (startSegments.isEmpty()) {
            return;
        }

        // Fetch all user segments once to avoid N+1 queries
        Map<String, BudgetSegment> segmentMap = budgetSegmentRepo.findAllByUserId(userId).stream()
                .collect(Collectors.toMap(BudgetSegment::getSegmentId, Function.identity()));

        // Track processed segments to avoid duplicates
        Set<String> processedIds = new HashSet<>();
        List<BudgetSegment> segmentsToSave = new ArrayList<>();

        for (BudgetSegment startSegment : startSegments) {
            String currentId = startSegment.getSegmentId();

            while (currentId != null && !processedIds.contains(currentId)) {
                processedIds.add(currentId);
                BudgetSegment segment = segmentMap.get(currentId);

                if (segment == null) {
                    break;
                }

                // Update spent amount
                segment.setSpentAmount(segment.getSpentAmount() + amount);
                segmentsToSave.add(segment);

                // Stop if we've reached the root
                if (segment.getParentSegmentId() == null) {
                    break;
                }

                // Move up to parent
                currentId = segment.getParentSegmentId();
            }
        }

        // Batch save
        if (!segmentsToSave.isEmpty()) {
            budgetSegmentRepo.saveAll(segmentsToSave);
        }
    }

    @Async
    public void updateBudgetForTransactionUpdate(String categoryId, Double oldAmount, Double newAmount) {
        Double amountDifference = newAmount - oldAmount;
        updateBudget(categoryId, amountDifference);
    }

    @Async
    public void updateBudgetForDeleteTransaction(String categoryId, Double amount) {
        updateBudget(categoryId, -amount);
    }
}
