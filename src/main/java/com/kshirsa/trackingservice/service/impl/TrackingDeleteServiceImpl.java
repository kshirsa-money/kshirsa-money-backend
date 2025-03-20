package com.kshirsa.trackingservice.service.impl;

import com.kshirsa.budgetingservice.service.impl.BudgetProcessService;
import com.kshirsa.coreservice.exception.CustomException;
import com.kshirsa.coreservice.exception.ErrorCode;
import com.kshirsa.trackingservice.entity.Transactions;
import com.kshirsa.trackingservice.repository.CategoryRepo;
import com.kshirsa.trackingservice.repository.TransactionRepo;
import com.kshirsa.trackingservice.service.AsyncService;
import com.kshirsa.trackingservice.service.declaration.TrackingDeleteService;
import com.kshirsa.userservice.service.declaration.UserDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = DataIntegrityViolationException.class)
@RequiredArgsConstructor
public class TrackingDeleteServiceImpl implements TrackingDeleteService {

    private final CategoryRepo categoryRepo;
    private final TransactionRepo transactionRepo;
    private final UserDetailsService userDetailsService;
    private final AsyncService asyncService;
    private final BudgetProcessService budgetProcessService;

    @Override
    public void deleteCategory(String categoryId) {
        categoryRepo.deleteById(categoryId);
    }

    @Override
    public void deleteTransaction(String transactionId) throws CustomException {
        Transactions transaction = transactionRepo.findById(transactionId)
                .orElseThrow(()-> new CustomException(ErrorCode.INVALID_TRANSACTION_ID.name()));       // Checking Transaction selected valid or not
        transactionRepo.deleteById(transactionId);                                                     // Deleting Transaction
        budgetProcessService.updateBudgetForDeleteTransaction(transaction.getCategory().getCategoryId(), transaction.getAmount()); // Updating Budget
        asyncService.updateHashTags(userDetailsService.getUser());
    }
}
