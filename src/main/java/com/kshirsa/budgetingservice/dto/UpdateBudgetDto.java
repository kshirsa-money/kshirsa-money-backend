package com.kshirsa.budgetingservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateBudgetDto {

    private String segmentId;
    private String segmentName;
    private Double allocatedAmount;
    private Integer alertPercentage;
    private List<String> transactionCategories;
}
