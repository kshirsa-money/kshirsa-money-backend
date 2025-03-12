package com.kshirsa.budgetingservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.YearMonth;

@Data
@Entity
public class BudgetHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String historyId;
    private String segmentId;
    private String userId;
    private String segmentName;
    private Double allocatedAmount;
    private Double spentAmount;
    private Integer alertPercentage;
    private Boolean isTotalBudget;
    private YearMonth budgetMonth;
    @CreationTimestamp
    @Column(updatable = false)
    private Instant recordedOn;
}
