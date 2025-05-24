package com.kshirsa.trackingservice.service.declaration;

import com.kshirsa.coreservice.exception.CustomException;
import com.kshirsa.trackingservice.dto.request.UpdateCategory;
import com.kshirsa.trackingservice.dto.request.UpdateLoanRepayment;
import com.kshirsa.trackingservice.dto.request.UpdateTransaction;
import com.kshirsa.trackingservice.entity.Category;
import com.kshirsa.trackingservice.entity.LoanRepayment;
import com.kshirsa.trackingservice.entity.Transactions;

public interface TrackingUpdateService {
    Category updateCategory(UpdateCategory category) throws CustomException;

    Transactions updateTransaction(UpdateTransaction updateTransaction) throws CustomException;

    LoanRepayment updateLoanRepayment(UpdateLoanRepayment updateLoanRepayment) throws CustomException;
}
