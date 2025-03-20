package com.kshirsa.trackingservice.service.impl;

import com.kshirsa.budgetingservice.service.impl.BudgetProcessService;
import com.kshirsa.coreservice.exception.CustomException;
import com.kshirsa.coreservice.exception.ErrorCode;
import com.kshirsa.trackingservice.dto.request.UpdateCategory;
import com.kshirsa.trackingservice.dto.request.UpdateLoanRepayment;
import com.kshirsa.trackingservice.dto.request.UpdateTransaction;
import com.kshirsa.trackingservice.entity.*;
import com.kshirsa.trackingservice.entity.enums.TransactionType;
import com.kshirsa.trackingservice.repository.*;
import com.kshirsa.trackingservice.service.AsyncService;
import com.kshirsa.trackingservice.service.declaration.TrackingUpdateService;
import com.kshirsa.userservice.service.declaration.UserDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TrackingUpdateServiceImpl implements TrackingUpdateService {

    private final CategoryRepo categoryRepo;
    private final TransactionRepo transactionRepo;
    private final LoanDetailsRepo loanDetailsRepo;
    private final LoanRepaymentRepo loanRepaymentRepo;
    private final UserDetailsService userDetailsService;
    private final AsyncService asyncService;
    private final BudgetProcessService budgetProcessService;

    @Override
    public Category updateCategory(UpdateCategory categoryDto) throws CustomException {
        Category category = categoryRepo.findById(categoryDto.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CATEGORY.name()));
        category.setCategoryName(categoryDto.getCategoryName());
        category.setDescription(categoryDto.getDescription());
        return categoryRepo.save(category);
    }

    @Override
    public Transactions updateTransaction(UpdateTransaction updateTransaction) throws CustomException {

        boolean isCategoryChanged;

        Category category = categoryRepo.findById(updateTransaction.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CATEGORY.name()));                             // Checking Category selected valid or not
        if (!category.getTransactionType().equals(updateTransaction.getTransactionType()))
            throw new CustomException(ErrorCode.WRONG_TRANSACTION_TYPE.name());                                         // Checking Transaction Type selected valid or not
        if (updateTransaction.getTransactionType().equals(TransactionType.LOAN) && updateTransaction.getLoanDetails() == null)
            throw new CustomException(ErrorCode.LOAN_DETAILS_REQUIRED.name());                                          // Checking if Loan Details are present

        Transactions oldTransactions = transactionRepo.findById(updateTransaction.getTransactionId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TRANSACTION_ID.name()));
        Transactions transactions = oldTransactions;

        isCategoryChanged = !transactions.getCategory().getCategoryId().equals(category.getCategoryId());

        transactions.setAmount(updateTransaction.getAmount());
        transactions.setPaymentMode(updateTransaction.getPaymentMode());
        transactions.setNote(updateTransaction.getNote());
        transactions.setTransactionTime(updateTransaction.getTransactionTime());
        transactions.setIsRecurring(updateTransaction.getIsRecurring());
        transactions.setCategory(category);

        if (transactions.getTransactionType().equals(TransactionType.LOAN) && !updateTransaction.getTransactionType().equals(TransactionType.LOAN)) {
            loanDetailsRepo.deleteById(transactions.getLoanDetails().getLoanId());                                      // Deleting Loan Details if transaction type is changed from Loan
        }
        transactions.setTransactionType(updateTransaction.getTransactionType());

        if (transactions.getTransactionType().equals(TransactionType.LOAN)) {                                           // Checking if transaction type is Loan
            LoanDetails loanDetails = transactions.getLoanDetails();
            //noinspection DataFlowIssue
            loanDetails.setExpectedPaymentDate(updateTransaction.getLoanDetails().expectedPaymentDate());
            loanDetails.setTransactingParty(updateTransaction.getLoanDetails().transactingParty());
            transactions.setLoanDetails(loanDetails);                                                                   // Updating Loan Details
        }

        transactions.setTags(updateTransaction.getTags());
        transactions = transactionRepo.save(transactions);

        if (isCategoryChanged) {
            budgetProcessService.updateBudgetForDeleteTransaction(oldTransactions.getCategory().getCategoryId(),
                    oldTransactions.getAmount());                                                                       // Deleting Budget for old category, as category is changed
            budgetProcessService.updateBudget(transactions.getCategory().getCategoryId(), transactions.getAmount());    // Updating Budget for new category
        } else
            budgetProcessService.updateBudgetForTransactionUpdate(transactions.getCategory().getCategoryId(),
                    oldTransactions.getAmount(), transactions.getAmount());                                             // Updating Budget for transaction update

        asyncService.updateHashTags(userDetailsService.getUser());                                                      // Updating Hash Tags in hash_tags table

        return transactions;
    }

    @Override
    public LoanRepayment updateLoanRepayment(UpdateLoanRepayment updateLoanRepayment) throws CustomException {
        LoanRepayment loanRepayment = loanRepaymentRepo.findById(updateLoanRepayment.loanRepaymentId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_LOAN_REPAYMENT_ID.name()));

        loanRepayment.setPaymentDate(updateLoanRepayment.paymentDate());
        loanRepayment.setNote(updateLoanRepayment.note());

        loanRepayment.getLoanDetails().setPaidAmount(loanRepayment.getLoanDetails().getPaidAmount()
                - loanRepayment.getAmount() + updateLoanRepayment.amount());                        // Updating Paid Amount
        loanRepayment.getLoanDetails().setOutstandingAmount(loanRepayment.getLoanDetails().getOutstandingAmount()
                + loanRepayment.getAmount() - updateLoanRepayment.amount());                        // Updating Outstanding Amount
        loanRepayment.setAmount(updateLoanRepayment.amount());                                      // Updating Loan Repayment Amount

        loanDetailsRepo.save(loanRepayment.getLoanDetails());                                       // Saving Loan Details
        return loanRepaymentRepo.save(loanRepayment);                                               // Saving Loan Repayment
    }
}
