package com.kshirsa.trackingservice.dto.response;

import com.kshirsa.trackingservice.entity.Category;

public record CategoryResponse(Category category, Boolean isInUse, Boolean isDefault) {
}
