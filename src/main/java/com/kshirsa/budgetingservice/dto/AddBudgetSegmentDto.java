package com.kshirsa.budgetingservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddBudgetSegmentDto {
    private String parentSegmentId;
    private String segmentName;
    private Double allocatedAmount;
    private Double spentAmount;
    private Integer alertPercentage;
    private Boolean isTotalBudget;
    private List<String> transactionCategories;
}
