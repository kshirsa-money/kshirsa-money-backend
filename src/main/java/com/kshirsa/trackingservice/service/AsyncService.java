package com.kshirsa.trackingservice.service;

import com.kshirsa.trackingservice.entity.HashTags;
import com.kshirsa.trackingservice.repository.HashTagRepo;
import com.kshirsa.trackingservice.repository.TransactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncService {

    private final TransactionRepo transactionRepo;
    private final HashTagRepo hashTagRepo;

    @Async
    public void updateHashTags(String userId) {
        hashTagRepo.save(new HashTags(userId,transactionRepo.findHashTagsByUserId(userId)));
    }
}
