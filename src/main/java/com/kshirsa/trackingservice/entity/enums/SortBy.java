package com.kshirsa.trackingservice.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
@Getter
public enum SortBy {
    Latest("transaction_time", Sort.Direction.DESC),
    Oldest("transaction_time", Sort.Direction.ASC),
    AmountHighToLow("amount", Sort.Direction.DESC),
    AmountLowToHigh("amount", Sort.Direction.ASC);

    private final String sortBy;
    private final Sort.Direction sortDirection;

}
