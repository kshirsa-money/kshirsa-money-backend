package com.kshirsa.trackingservice.dto.request;

import java.time.LocalDateTime;

public record UpdateLoanRepayment(String loanRepaymentId,
                                  LocalDateTime paymentDate,
                                  Double amount,
                                  String note) {
}
