package com.kshirsa.trackingservice.dto.request;

import com.kshirsa.trackingservice.entity.enums.TransactionType;
import jakarta.validation.constraints.NotNull;

public record AddCategory(@NotNull String categoryName,String description,
                          @NotNull TransactionType transactionType) {
}
