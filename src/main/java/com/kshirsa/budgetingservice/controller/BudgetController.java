package com.kshirsa.budgetingservice.controller;

import com.kshirsa.budgetingservice.dto.AddBudgetSegmentDto;
import com.kshirsa.budgetingservice.dto.UpdateBudgetDto;
import com.kshirsa.budgetingservice.entity.BudgetSegment;
import com.kshirsa.budgetingservice.service.declaration.AddBudgetService;
import com.kshirsa.coreservice.BaseConstants;
import com.kshirsa.coreservice.BaseController;
import com.kshirsa.coreservice.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = BaseConstants.ROOT_PATH + "/budget", produces = "application/json")
@Validated
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "7. Budget Add Controller", description = "APIs for adding money tracking data")
public class BudgetController extends BaseController {

    private final AddBudgetService addBudgetService;
    private final Environment env;

    @Operation(summary = "Add Budget Segment", description = "Add a new budget segment")
    @PostMapping("/add/segment")
    public ResponseEntity<SuccessResponse<BudgetSegment>> addBudgetSegment(@RequestBody AddBudgetSegmentDto addBudgetSegmentDto) {
        return ok(addBudgetService.addBudgetSegment(addBudgetSegmentDto), env.getProperty("BUDGET.SEGMENT.ADDED"));
    }

    @Operation(summary = "Get Budget Segment", description = "Get an existing budget segment")
    @GetMapping("/get/segment/{segmentId}")
    public ResponseEntity<SuccessResponse<BudgetSegment>> getBudgetSegment(@PathVariable String segmentId) {
        return ok(addBudgetService.getBudgetSegment(segmentId), env.getProperty("BUDGET.SEGMENT.RETRIEVED"));
    }

    @Operation(summary = "Update Budget Segment", description = "Update an existing budget segment")
    @PutMapping("/update/segment")
    public ResponseEntity<SuccessResponse<BudgetSegment>> updateBudgetSegment(@RequestBody UpdateBudgetDto updateSegmentDto) {
        return ok(addBudgetService.updateBudgetSegment(updateSegmentDto), env.getProperty("BUDGET.SEGMENT.UPDATED"));
    }

    @Operation(summary = "Delete Budget Segment", description = "Delete an existing budget segment & it's nested child segments")
    @DeleteMapping("/delete/{segmentId}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteBudgetSegment(@PathVariable String segmentId) {
        return ok(addBudgetService.deleteBudgetSegment(segmentId), env.getProperty("BUDGET.SEGMENT.DELETED"));
    }
}