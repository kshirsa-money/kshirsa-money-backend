package com.kshirsa.trackingservice.controller;

import com.kshirsa.coreservice.BaseConstants;
import com.kshirsa.coreservice.BaseController;
import com.kshirsa.coreservice.SuccessResponse;
import com.kshirsa.coreservice.exception.CustomException;
import com.kshirsa.trackingservice.dto.request.AddCategory;
import com.kshirsa.trackingservice.dto.request.AddLoanRepayment;
import com.kshirsa.trackingservice.dto.request.AddTransaction;
import com.kshirsa.trackingservice.entity.Category;
import com.kshirsa.trackingservice.entity.LoanRepayment;
import com.kshirsa.trackingservice.entity.Transactions;
import com.kshirsa.trackingservice.service.declaration.TrackingAddService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = BaseConstants.ROOT_PATH + "/track/add", produces = "application/json")
@Validated
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "3. Tracking Add Controller", description = "APIs for adding money tracking data")
public class TrackingAddController extends BaseController {

    private final TrackingAddService trackingAddService;
    private final Environment env;

    @Operation(summary = "Add a new category")
    @PostMapping("/category")
    public ResponseEntity<SuccessResponse<Category>> addCategory(@RequestBody AddCategory category) {
        return ok(trackingAddService.addCategory(category), env.getProperty("CATEGORY.ADD.SUCCESS"));
    }

    @Operation(summary = "Add a new transaction", description = "transactionType : EXPENSE, INCOME, LOAN; paymentMode : CASH, DEBIT_CARD, CREDIT_CARD, UPI, NET_BANKING;")
    @PostMapping("/transaction")
    public ResponseEntity<SuccessResponse<Transactions>> addTransaction(@RequestBody @Valid AddTransaction transaction) throws CustomException {
        return ok(trackingAddService.addTransaction(transaction), env.getProperty("TRANSACTION.ADD.SUCCESS"));
    }

    @Operation(summary = "Add a new loan repayment")
    @PostMapping("/loan-repayment")
    public ResponseEntity<SuccessResponse<LoanRepayment>> addLoanRepayment(@RequestBody @Valid AddLoanRepayment loanRepayment) throws CustomException {
        return ok(trackingAddService.addLoanRepayment(loanRepayment), env.getProperty("LOAN.REPAYMENT.ADD.SUCCESS"));
    }
}