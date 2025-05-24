package com.kshirsa.trackingservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCategory {
    @NotNull
    private String categoryId;
    private String categoryName;
    private String description;
}
