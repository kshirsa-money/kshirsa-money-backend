package com.kshirsa.trackingservice.controller;

import com.kshirsa.coreservice.BaseConstants;
import com.kshirsa.coreservice.BaseController;
import com.kshirsa.coreservice.SuccessResponse;
import com.kshirsa.coreservice.exception.CustomException;
import com.kshirsa.coreservice.exception.ErrorCode;
import com.kshirsa.trackingservice.service.declaration.TrackingDeleteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = BaseConstants.ROOT_PATH + "/track/delete", produces = "application/json")
@Validated
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "6. Tracking Delete Controller", description = "APIs for deleting money tracking data")
public class TrackingDeleteController extends BaseController {

    private final TrackingDeleteService trackingDeleteService;
    private final Environment env;

    @Operation(summary = "Delete a category", description = "This will delete the category. But if the category is in use, it will throw an error(code 806).")
    @DeleteMapping("/category")
    public ResponseEntity<SuccessResponse<Void>> deleteCategory(@RequestParam String categoryId) throws CustomException {
        try {
            trackingDeleteService.deleteCategory(categoryId);
            return ok(null, env.getProperty("CATEGORY.DELETE.SUCCESS"));
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.CATEGORY_IN_USE.name());
        }
    }

    @Operation(summary = "Delete a transaction")
    @DeleteMapping("/transaction")
    public ResponseEntity<SuccessResponse<Void>> deleteTransaction(@RequestParam String transactionId) throws CustomException {
        trackingDeleteService.deleteTransaction(transactionId);
        return ok(null, env.getProperty("TRANSACTION.DELETE.SUCCESS"));
    }
}
