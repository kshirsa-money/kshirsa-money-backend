package com.kshirsa.trackingservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AllCategoryResponse {

    @JsonProperty("EXPENSE")
    private List<CategoryResponse> EXPENSE;

    @JsonProperty("INCOME")
    private List<CategoryResponse> INCOME;

    @JsonProperty("LOAN")
    private List<CategoryResponse> LOAN;
}