package com.kshirsa.budgetingservice.controller;

import com.kshirsa.budgetingservice.dto.AddBudgetSegmentDto;
import com.kshirsa.budgetingservice.entity.BudgetSegment;
import com.kshirsa.budgetingservice.service.declaration.AddBudgetService;
import com.kshirsa.coreservice.BaseConstants;
import com.kshirsa.coreservice.BaseController;
import com.kshirsa.coreservice.SuccessResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = BaseConstants.ROOT_PATH + "/budget/add", produces = "application/json")
@Validated
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "7. Budget Add Controller", description = "APIs for adding money tracking data")
public class BudgetAddController extends BaseController {

     private final AddBudgetService addBudgetService;
     private final Environment env;

     @PostMapping("/segment")
     public ResponseEntity<SuccessResponse<BudgetSegment>> addBudgetSegment(@RequestBody AddBudgetSegmentDto addBudgetSegmentDto) {
         return ok(addBudgetService.addBudgetSegment(addBudgetSegmentDto), env.getProperty("BUDGET.SEGMENT.ADDED"));
     }

}
