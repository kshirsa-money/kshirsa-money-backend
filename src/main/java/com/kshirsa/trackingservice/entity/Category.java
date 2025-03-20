package com.kshirsa.trackingservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kshirsa.budgetingservice.entity.BudgetSegment;
import com.kshirsa.trackingservice.dto.request.AddCategory;
import com.kshirsa.trackingservice.entity.enums.TransactionType;
import com.kshirsa.utility.IdGenerator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Category {

    @Id
    private String categoryId;
    private String categoryName;
    private String description;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private String createdBy;
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Transactions> transactions;
    @ManyToMany(mappedBy = "transactionCategories")
    @JsonIgnore
    private List<BudgetSegment> budgetSegments;

    public Category(AddCategory addCategory) {
        this.categoryId = IdGenerator.generateCategoryId();
        this.categoryName = addCategory.categoryName();
        this.description = addCategory.description();
        this.transactionType = addCategory.transactionType();
    }
}
