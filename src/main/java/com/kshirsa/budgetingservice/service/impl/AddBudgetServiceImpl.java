package com.kshirsa.budgetingservice.service.impl;

import com.kshirsa.budgetingservice.dto.AddBudgetSegmentDto;
import com.kshirsa.budgetingservice.dto.UpdateBudgetDto;
import com.kshirsa.budgetingservice.entity.BudgetSegment;
import com.kshirsa.budgetingservice.repository.BudgetSegmentRepo;
import com.kshirsa.budgetingservice.service.declaration.AddBudgetService;
import com.kshirsa.userservice.service.declaration.UserDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public BudgetSegment getBudgetSegment(String segmentId) {
        return budgetSegmentRepo.findById(segmentId).orElseThrow();
    }

    @Override
    public BudgetSegment updateBudgetSegment(UpdateBudgetDto updateSegmentDto) {
        BudgetSegment budgetSegment = budgetSegmentRepo.findById(updateSegmentDto.getSegmentId()).orElseThrow();
        budgetSegment.setSegmentName(updateSegmentDto.getSegmentName());
        budgetSegment.setAllocatedAmount(updateSegmentDto.getAllocatedAmount());
        budgetSegment.setAlertPercentage(updateSegmentDto.getAlertPercentage());
        budgetSegment.setTransactionCategories(updateSegmentDto.getTransactionCategories());
        return budgetSegmentRepo.save(budgetSegment);
    }

    @Override
    public Boolean deleteBudgetSegment(String segmentId) {
        List<BudgetSegment> childSegments = budgetSegmentRepo.findAllByParentSegmentId(segmentId);
        for (BudgetSegment childSegment : childSegments) {
            deleteBudgetSegment(childSegment.getSegmentId());
        }
        budgetSegmentRepo.deleteById(segmentId);
        return true;
    }
}
