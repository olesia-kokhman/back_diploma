package com.backenddiploma.dto.budget;

import com.backenddiploma.models.enums.BudgetType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BudgetCreateDTO {

    private Long userId;
    private Long categoryId;
    private BudgetType type;
    private double plannedAmount;
    private double actualAmount;
    private double availableAmount;
    private String currency;
    private LocalDate periodStart;
    private LocalDate periodEnd;
}
