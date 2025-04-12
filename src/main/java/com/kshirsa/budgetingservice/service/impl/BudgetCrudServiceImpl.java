package com.kshirsa.budgetingservice.service.impl;

import com.kshirsa.budgetingservice.dto.AddBudgetSegmentDto;
import com.kshirsa.budgetingservice.dto.UpdateBudgetDto;
import com.kshirsa.budgetingservice.entity.BudgetSegment;
import com.kshirsa.budgetingservice.repository.BudgetSegmentRepo;
import com.kshirsa.budgetingservice.service.declaration.BudgetCrudService;
import com.kshirsa.trackingservice.entity.Category;
import com.kshirsa.trackingservice.repository.CategoryRepo;
import com.kshirsa.userservice.service.declaration.UserDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BudgetCrudServiceImpl implements BudgetCrudService {

    private final BudgetSegmentRepo budgetSegmentRepo;
    private final CategoryRepo categoryRepo;
    private final BudgetProcessService budgetProcessService;
    private final UserDetailsService userDetailsService;

    @Override
    public BudgetSegment addBudgetSegment(AddBudgetSegmentDto addBudgetSegmentDto) {
        List<Category> categories = new ArrayList<>();
        BudgetSegment budgetSegment = BudgetSegment.budgetSegmentDtoToEntity(addBudgetSegmentDto);
        budgetSegment.setUserId(userDetailsService.getUser());
        addBudgetSegmentDto.getTransactionCategories().forEach(
                catId -> categories.add(categoryRepo.findById(catId)
                        .orElseThrow()));                                           // Getting all the category entities or throw if invalid id
        budgetSegment.setTransactionCategories(categories);                         // adding categories to the segments

        budgetSegment = budgetSegmentRepo.save(budgetSegment);

        budgetProcessService.initializeBudget(budgetSegment);

        return budgetSegment;
    }

    @Override
    public BudgetSegment getBudgetSegment(String segmentId) {
        return budgetSegmentRepo.findById(segmentId).orElseThrow();
    }

    @Override
    public BudgetSegment updateBudgetSegment(UpdateBudgetDto updateSegmentDto) {
        List<Category> categories = new ArrayList<>();

        BudgetSegment budgetSegment = budgetSegmentRepo.findById(updateSegmentDto.getSegmentId()).orElseThrow();
        budgetSegment.setSegmentName(updateSegmentDto.getSegmentName());
        budgetSegment.setAllocatedAmount(updateSegmentDto.getAllocatedAmount());
        budgetSegment.setAlertPercentage(updateSegmentDto.getAlertPercentage());
        updateSegmentDto.getTransactionCategories().forEach(catId -> categories.add(categoryRepo.findById(catId).orElseThrow()));
        budgetSegment.setTransactionCategories(categories);

        budgetSegment = budgetSegmentRepo.save(budgetSegment);

        budgetProcessService.initializeBudget(budgetSegment);

        return budgetSegment;
    }

    @Override
    public Boolean deleteBudgetSegment(String segmentId) {
        List<BudgetSegment> childSegments = budgetSegmentRepo.findAllByParentSegmentId(segmentId);      // Getting all the child segments of this parent segment
        for (BudgetSegment childSegment : childSegments) {
            deleteBudgetSegment(childSegment.getSegmentId());                                           // Recursive call to find all the child segments of the segments
        }
        budgetSegmentRepo.deleteById(segmentId);
        return true;
    }
}
