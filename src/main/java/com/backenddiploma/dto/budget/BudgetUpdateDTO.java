package com.backenddiploma.dto.budget;

import com.backenddiploma.models.enums.BudgetType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BudgetUpdateDTO {

    private Long categoryId;
    private BudgetType type;
    private Double plannedAmount;
    private Double actualAmount;
    private Double availableAmount;
    private String currency;
    private LocalDate periodStart;
    private LocalDate periodEnd;
}
