package com.kshirsa.trackingservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kshirsa.trackingservice.entity.enums.PaymentMode;
import com.kshirsa.trackingservice.entity.enums.TransactionType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class UpdateTransaction {

    public record UpdateLoanDetails(
            LocalDate expectedPaymentDate,
            String transactingParty) {
    }

    @NotNull
    private String transactionId;
    private Double amount;
    private PaymentMode paymentMode;
    private String note;
    private TransactionType transactionType;
    private LocalDateTime transactionTime;
    private String categoryId;
    private Boolean isRecurring;
    private Set<String> tags;
    @Nullable
    private UpdateLoanDetails loanDetails;
}
