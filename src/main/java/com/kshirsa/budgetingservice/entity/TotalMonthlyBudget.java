package com.kshirsa.budgetingservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@Entity
public class TotalMonthlyBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String totalBudgetId;
    private String userId;
    private Double monthlyBudget;
    @Column(updatable = false)
    @CreationTimestamp
    private Instant createdOn;
    @UpdateTimestamp
    private Instant updatedOn;
}
