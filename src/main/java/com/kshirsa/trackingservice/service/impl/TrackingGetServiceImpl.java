package com.kshirsa.trackingservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kshirsa.coreservice.exception.CustomException;
import com.kshirsa.coreservice.exception.ErrorCode;
import com.kshirsa.trackingservice.dto.TrackingFilter;
import com.kshirsa.trackingservice.dto.response.CategoryResponse;
import com.kshirsa.trackingservice.dto.response.TrackingFilterRes;
import com.kshirsa.trackingservice.dto.response.ViewTransaction;
import com.kshirsa.trackingservice.entity.Category;
import com.kshirsa.trackingservice.entity.Transactions;
import com.kshirsa.trackingservice.entity.enums.PaymentMode;
import com.kshirsa.trackingservice.entity.enums.SortBy;
import com.kshirsa.trackingservice.entity.enums.TransactionType;
import com.kshirsa.trackingservice.repository.CategoryRepo;
import com.kshirsa.trackingservice.repository.HashTagRepo;
import com.kshirsa.trackingservice.repository.TransactionRepo;
import com.kshirsa.trackingservice.service.declaration.TrackingGetService;
import com.kshirsa.userservice.service.declaration.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TrackingGetServiceImpl implements TrackingGetService {

    private final CategoryRepo categoryRepo;
    private final UserDetailsService userDetailsService;
    private final TransactionRepo transactionRepo;
    private final HashTagRepo hashTagRepo;
    private final ObjectMapper objectMapper;

    @Override
    public List<CategoryResponse> getCategory(TransactionType type) {
        List<Category> categoryList = categoryRepo.getAllCategory(userDetailsService.getUser(), type.name());
        List<CategoryResponse> categoryResponse = new ArrayList<>();
        if (categoryList != null) {
            categoryList.forEach(category -> categoryResponse.add(new CategoryResponse(category,
                                    category.getCreatedBy().equalsIgnoreCase("SYSTEM") || !category.getTransactions().isEmpty()
                            )
                    )
            );
        }
        return categoryResponse;
    }

    @Override
    public Object getHashTags() throws JsonProcessingException {
        return objectMapper.readValue((String) hashTagRepo.findHashTagsByUserId(userDetailsService.getUser()).orElseGet(String::new), Set.class);
    }

    @Override
    public List<ViewTransaction> getRecentTransaction() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Transactions> transactionsList = transactionRepo.findAllByUserId(userDetailsService.getUser(),
                pageRequest.withSort(SortBy.Latest.getSortDirection(), SortBy.Latest.getSortBy()));
        if (transactionsList != null)
            return transactionsList.stream().map(TrackingGetServiceImpl::convertToViewTransaction).toList();
        return null;
    }

    @Override
    public List<ViewTransaction> getTransaction(TrackingFilter filter, Integer pageNumber, Integer transactionPerPage, SortBy sortBy) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, transactionPerPage);
        if (filter == null)
            filter = new TrackingFilter();

        List<Transactions> transactions = transactionRepo.findTransactions(filter.getCategory(), filter.getPaymentMode(),
                filter.getTransactionType(), filter.getAmountMax(), filter.getAmountMin(), filter.getTransactionAfter(),
                filter.getTransactionBefore(), filter.getHashTag(), userDetailsService.getUser(),
                pageRequest.withSort(sortBy.getSortDirection(), sortBy.getSortBy()));

        if (transactions != null)
            return transactions.stream().map(TrackingGetServiceImpl::convertToViewTransaction).toList();

        return new ArrayList<>();
    }

    @Override
    public TrackingFilterRes getTransactionFilter() throws JsonProcessingException {

        String userId = userDetailsService.getUser();
        String jsonString = (String) hashTagRepo.findHashTagsByUserId(userId).orElseGet(String::new);

        return TrackingFilterRes.builder()
                .categories(categoryRepo.getCategoryByUserId(userId))
                .paymentModes(new HashSet<>(List.of(PaymentMode.values())))
                .transactionTypes(new HashSet<>(List.of(TransactionType.values())))
                .hashtags(objectMapper.readValue(jsonString, Set.class)).build();
    }

    @Override
    public Transactions getSingleTransaction(String transactionId) throws CustomException {
        return transactionRepo.findById(transactionId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_TRANSACTION_ID.name()));
    }

    public static ViewTransaction convertToViewTransaction(Transactions transaction) {
        return new ViewTransaction(
                transaction.getTransactionId(), transaction.getAmount(), transaction.getPaymentMode(),
                transaction.getNote(), transaction.getTransactionType(), transaction.getTransactionTime(),
                transaction.getCategory().getCategoryName(),
                transaction.getCategory().getCategoryId(),
                transaction.getTags(),
                transaction.getLoanDetails() != null ? transaction.getLoanDetails().getIsSettled() : null,
                transaction.getLoanDetails() != null ? transaction.getLoanDetails().getOutstandingAmount() : null);
    }
}
