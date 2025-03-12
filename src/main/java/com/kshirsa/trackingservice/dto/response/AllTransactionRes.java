package com.kshirsa.trackingservice.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class AllTransactionRes {

    private List<ViewTransaction> transactionList;
    private int totalPages;
    private int currentPage;
    private long totalTransactionCount;
    private int currentPageTransactionCount;
    private boolean hasNextPage;
}
