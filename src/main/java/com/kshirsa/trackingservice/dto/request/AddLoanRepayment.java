package com.kshirsa.trackingservice.dto.request;

import java.time.LocalDateTime;

public record AddLoanRepayment(String transactionId,
                               LocalDateTime paymentDate,
                               Double amount, String note) {
}
