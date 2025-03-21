package com.kshirsa.budgetingservice.entity;

import com.kshirsa.budgetingservice.dto.AddBudgetSegmentDto;
import com.kshirsa.trackingservice.entity.Category;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.YearMonth;
import java.util.List;

@Data
@Entity
public class BudgetSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String segmentId;
    private String userId;
    private String parentSegmentId;
    private String segmentName;
    private Double allocatedAmount;
    private Double spentAmount = 0.0;
    private Integer alertPercentage = 80;
    private Boolean isTotalBudget;
    @ManyToMany
    @JoinTable(
            name = "segment_category",
            joinColumns = @JoinColumn(name = "join_segment_id"),
            inverseJoinColumns = @JoinColumn(name = "join_category_id")
    )
    private List<Category> transactionCategories;
    @CreationTimestamp
    @Column(updatable =false)
    private Instant createdOn;
    @UpdateTimestamp
    private Instant updatedOn;

    public static BudgetSegment budgetSegmentDtoToEntity(AddBudgetSegmentDto addBudgetSegmentDto) {
        BudgetSegment budgetSegment = new BudgetSegment();
        budgetSegment.setParentSegmentId(addBudgetSegmentDto.getParentSegmentId());
        budgetSegment.setSegmentName(addBudgetSegmentDto.getSegmentName());
        budgetSegment.setAllocatedAmount(addBudgetSegmentDto.getAllocatedAmount());
        budgetSegment.setAlertPercentage(addBudgetSegmentDto.getAlertPercentage());
        budgetSegment.setIsTotalBudget(addBudgetSegmentDto.getIsTotalBudget());
        return budgetSegment;
    }

    public static BudgetHistory convertToBudgetHistory(BudgetSegment budgetSegment) {
        BudgetHistory budgetHistory = new BudgetHistory();
        budgetHistory.setSegmentId(budgetSegment.getSegmentId());
        budgetHistory.setUserId(budgetSegment.getUserId());
        budgetHistory.setSegmentName(budgetSegment.getSegmentName());
        budgetHistory.setAllocatedAmount(budgetSegment.getAllocatedAmount());
        budgetHistory.setSpentAmount(budgetSegment.getSpentAmount());
        budgetHistory.setIsTotalBudget(budgetSegment.getIsTotalBudget());
        budgetHistory.setBudgetMonth(YearMonth.now());
        return budgetHistory;
    }
}
