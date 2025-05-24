package com.kshirsa.trackingservice.dto.request;

import com.kshirsa.trackingservice.entity.enums.PaymentMode;
import com.kshirsa.trackingservice.entity.enums.TransactionType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddTransaction {

    public record AddLoanDetails(LocalDate expectedPaymentDate,
                                 String transactingParty) {}

    @NotNull
    private Double amount;
    @NotNull
    private PaymentMode paymentMode;
    private String note;
    @NotNull
    private TransactionType transactionType;
    @NotNull
    private LocalDateTime transactionTime;
    @NotNull
    private String categoryId;
    private Boolean isRecurring;
    private Set<String> tags;
    @Nullable
    private AddLoanDetails loanDetails;
}
