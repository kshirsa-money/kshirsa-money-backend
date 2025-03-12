package com.kshirsa.trackingservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kshirsa.coreservice.BaseConstants;
import com.kshirsa.coreservice.BaseController;
import com.kshirsa.coreservice.SuccessResponse;
import com.kshirsa.coreservice.exception.CustomException;
import com.kshirsa.trackingservice.dto.TrackingFilter;
import com.kshirsa.trackingservice.dto.response.AllCategoryResponse;
import com.kshirsa.trackingservice.dto.response.AllTransactionRes;
import com.kshirsa.trackingservice.dto.response.TrackingFilterRes;
import com.kshirsa.trackingservice.dto.response.ViewTransaction;
import com.kshirsa.trackingservice.entity.Transactions;
import com.kshirsa.trackingservice.entity.enums.SortBy;
import com.kshirsa.trackingservice.entity.enums.TransactionType;
import com.kshirsa.trackingservice.service.declaration.TrackingGetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = BaseConstants.ROOT_PATH + "/track/get", produces = "application/json")
@Validated
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "4. Tracking Get Controller", description = "APIs for getting money tracking data")
public class TrackingGetController extends BaseController {

    private final TrackingGetService trackingGetService;
    private final Environment env;

    @Operation(summary = "Get all categories for a transaction type")
    @GetMapping("/category")
    public ResponseEntity<SuccessResponse<AllCategoryResponse>> getCategory() {
        return ok(trackingGetService.getCategory(), env.getProperty("CATEGORY.FETCH.SUCCESS"));
    }

    @Operation(summary = "Get a transaction by id")
    @GetMapping("transaction/{transactionId}")
    public ResponseEntity<SuccessResponse<Transactions>> getTransaction(@PathVariable String transactionId) throws CustomException {
        return ok(trackingGetService.getSingleTransaction(transactionId), env.getProperty("TRANSACTION.FETCH.SUCCESS"));
    }

    @Operation(summary = "Get last 10 transactions")
    @GetMapping("/transaction/recent")
    public ResponseEntity<SuccessResponse<List<ViewTransaction>>> getRecentTransaction() {
        return ok(trackingGetService.getRecentTransaction(), env.getProperty("RECENT.TRANSACTIONS.FETCH.SUCCESS"));
    }

    @Operation(summary = "Get transactions based on filters")
    @Parameter(name = "sortBy", description = "Sort results by 'Latest', 'Oldest', 'AmountHighToLow', 'AmountLowToHigh'")
    @GetMapping("/transaction")
    public ResponseEntity<SuccessResponse<AllTransactionRes>> getTransactions(@ParameterObject @ModelAttribute TrackingFilter filter,
                                                                              @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                                                                              @RequestParam(required = false, defaultValue = "6") Integer transactionPerPage,
                                                                              @RequestParam(required = false, defaultValue = "Latest") SortBy sortBy) {
        return ok(trackingGetService.getTransaction(filter, pageNumber, transactionPerPage, sortBy), env.getProperty("TRANSACTIONS.FETCH.SUCCESS"));
    }

    @Operation(summary = "Get transaction filters")
    @GetMapping("/transaction/filters")
    public ResponseEntity<SuccessResponse<TrackingFilterRes>> getTransactionFilters() throws JsonProcessingException {
        return ok(trackingGetService.getTransactionFilter(), env.getProperty("TRANSACTION.FILTERS.FETCH.SUCCESS"));
    }

    @Operation(summary = "Get all unique hashtags for the user")
    @GetMapping("/hashtags")
    public ResponseEntity<SuccessResponse<Object>> getHashTags() throws JsonProcessingException {
        return ok(trackingGetService.getHashTags(), env.getProperty("HASHTAGS.FETCH.SUCCESS"));
    }
}