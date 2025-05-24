package com.kshirsa.trackingservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kshirsa.coreservice.BaseConstants;
import com.kshirsa.trackingservice.entity.enums.PaymentMode;
import com.kshirsa.trackingservice.entity.enums.TransactionType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record TrackingFilterRes(Set<PaymentMode> paymentModes, Set<TransactionType> transactionTypes, Set<String> categories,
                                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BaseConstants.DATE_TIME_FORMAT)
                                LocalDateTime toDate,
                                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BaseConstants.DATE_TIME_FORMAT)
                                LocalDateTime fromDate,
                                Object hashtags) {
}
