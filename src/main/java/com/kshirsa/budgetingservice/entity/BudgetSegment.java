package com.kshirsa.budgetingservice.entity;

import com.kshirsa.budgetingservice.dto.AddBudgetSegmentDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
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
    private Double spentAmount;
    private Integer alertPercentage = 80;
    private Boolean isTotalBudget;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> transactionCategories;
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
        budgetSegment.setSpentAmount(addBudgetSegmentDto.getSpentAmount());
        budgetSegment.setAlertPercentage(addBudgetSegmentDto.getAlertPercentage());
        budgetSegment.setIsTotalBudget(addBudgetSegmentDto.getIsTotalBudget());
        budgetSegment.setTransactionCategories(addBudgetSegmentDto.getTransactionCategories());
        return budgetSegment;
    }
}
