package com.kshirsa.trackingservice.controller;

import com.kshirsa.coreservice.BaseConstants;
import com.kshirsa.coreservice.BaseController;
import com.kshirsa.coreservice.SuccessResponse;
import com.kshirsa.coreservice.exception.CustomException;
import com.kshirsa.trackingservice.dto.request.UpdateCategory;
import com.kshirsa.trackingservice.dto.request.UpdateLoanRepayment;
import com.kshirsa.trackingservice.dto.request.UpdateTransaction;
import com.kshirsa.trackingservice.entity.Category;
import com.kshirsa.trackingservice.entity.LoanRepayment;
import com.kshirsa.trackingservice.entity.Transactions;
import com.kshirsa.trackingservice.service.declaration.TrackingUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = BaseConstants.ROOT_PATH + "/track/update", produces = "application/json")
@Validated
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "5. Tracking Update Controller", description = "APIs for updating money tracking data")
public class TrackingUpdateController extends BaseController {

    private final TrackingUpdateService updateService;
    private final Environment env;

    @Operation(summary = "Update Category", description = "Update category details. Only category name and description can be updated")
    @PutMapping("/category")
    public ResponseEntity<SuccessResponse<Category>> updateCategory(@RequestBody UpdateCategory category) throws CustomException {
        return ok(updateService.updateCategory(category), env.getProperty("CATEGORY.UPDATE.SUCCESS"));
    }

    @Operation(summary = "Update Transaction", description = "Update transaction details.")
    @PutMapping("/transaction")
    public ResponseEntity<SuccessResponse<Transactions>> updateTransaction(@RequestBody UpdateTransaction updateTransaction) throws CustomException {
        return ok(updateService.updateTransaction(updateTransaction), env.getProperty("TRANSACTION.UPDATE.SUCCESS"));
    }

    @Operation(summary = "Update Loan Repayment", description = "Update loan repayment details.")
    @PutMapping("/loan-repayment")
    public ResponseEntity<SuccessResponse<LoanRepayment>> updateLoanRepayment(@RequestBody UpdateLoanRepayment updateLoanRepayment) throws CustomException {
        return ok(updateService.updateLoanRepayment(updateLoanRepayment), env.getProperty("LOAN.REPAYMENT.UPDATE.SUCCESS"));
    }
}