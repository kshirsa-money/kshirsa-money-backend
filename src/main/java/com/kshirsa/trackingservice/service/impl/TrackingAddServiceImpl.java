package com.kshirsa.trackingservice.service.impl;

import com.kshirsa.coreservice.exception.CustomException;
import com.kshirsa.coreservice.exception.ErrorCode;
import com.kshirsa.trackingservice.dto.request.AddCategory;
import com.kshirsa.trackingservice.dto.request.AddLoanRepayment;
import com.kshirsa.trackingservice.dto.request.AddTransaction;
import com.kshirsa.trackingservice.entity.*;
import com.kshirsa.trackingservice.entity.enums.TransactionType;
import com.kshirsa.trackingservice.repository.*;
import com.kshirsa.trackingservice.service.AsyncService;
import com.kshirsa.trackingservice.service.declaration.TrackingAddService;
import com.kshirsa.userservice.service.declaration.UserDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TrackingAddServiceImpl implements TrackingAddService {

    private final CategoryRepo categoryRepo;
    private final UserDetailsService userDetailsService;
    private final TransactionRepo transactionRepo;
    private final LoanDetailsRepo loanDetailsRepo;
    private final HashTagRepo hashTagRepo;
    private final LoanRepaymentRepo loanRepaymentRepo;
    private final AsyncService asyncService;

    @Override
    public Category addCategory(AddCategory categoryReq) {
        Category category = new Category(categoryReq);
        category.setCreatedBy(userDetailsService.getUser());
        return categoryRepo.save(category);
    }

    @Override
    public String addHashTag(String transactionId, String hashTag) throws CustomException {
        Transactions transaction = transactionRepo.findById(transactionId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TRANSACTION_ID.name()));
        transaction.getTags().add(hashTag);
        transactionRepo.save(transaction);
        asyncService.updateHashTags(userDetailsService.getUser());
        return hashTag;
    }

    @Override
    public Transactions addTransaction(AddTransaction transactionDto) throws CustomException {

        Category category = categoryRepo.findById(transactionDto.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CATEGORY.name()));        // Checking Category selected valid or not
        if (!category.getTransactionType().equals(transactionDto.getTransactionType()))
            throw new CustomException(ErrorCode.WRONG_TRANSACTION_TYPE.name());                    // Checking Transaction Type selected valid or not


        String userDetails = userDetailsService.getUser();                    // Getting User Details.(Not from db as it's already validated via jwt)

        Transactions transaction = transactionRepo
                .save(Transactions.transactionsDtoToEntity(transactionDto, category, userDetails)); // Saving Transaction

        if (!transaction.getTags().isEmpty()) {                                                     // Checking if tags are present
            Optional<HashTags> hashTags = hashTagRepo.findById(userDetails);

            if (hashTags.isEmpty())
                hashTagRepo.save(new HashTags(userDetails, transaction.getTags()));     // Creating new HashTags if not present
            else {
                transaction.getTags().forEach(tag -> hashTags.get().getHashTag().add(tag));     // Adding new tags to existing HashTags
                hashTagRepo.save(hashTags.get());
            }
        }

        if (transaction.getTransactionType().equals(TransactionType.LOAN)) {
            if (transactionDto.getLoanDetails() == null)
                throw new CustomException(ErrorCode.LOAN_DETAILS_REQUIRED.name());
            transaction.setLoanDetails(loanDetailsRepo.save(new LoanDetails(transactionDto.getLoanDetails(), transaction)));     // Saving Loan Details
        }

        return transaction;
    }

    @Override
    public LoanRepayment addLoanRepayment(AddLoanRepayment loanRepaymentDto) throws CustomException {
        LoanDetails loanDetails = loanDetailsRepo.findByTransaction_TransactionId(loanRepaymentDto.transactionId())
                .orElseThrow(() -> new CustomException(ErrorCode.NO_LOAN_DETAILS.name()));             // Checking if Loan Details are present

        LoanRepayment loanRepayment = new LoanRepayment();
        loanRepayment.setLoanDetails(loanDetails);
        loanRepayment.setAmount(loanRepaymentDto.amount());
        loanRepayment.setPaymentDate(loanRepaymentDto.paymentDate());
        loanRepayment.setNote(loanRepaymentDto.note());

        loanRepayment = loanRepaymentRepo.save(loanRepayment);                                          // Saving Loan Repayment

        loanDetails.setPaidAmount(loanDetails.getPaidAmount() + loanRepayment.getAmount());             // Updating Paid amount Details
        loanDetails.setOutstandingAmount(loanDetails.getTransaction().getAmount() - loanDetails.getPaidAmount());   // Updating outstanding amount Details
        if (loanDetails.getOutstandingAmount() <= 0)
            loanDetails.setIsSettled(true);

        loanRepayment.setLoanDetails(loanDetailsRepo.save(loanDetails));                                // Saving updated Loan Details & add that in response

        return loanRepayment;
    }
}
