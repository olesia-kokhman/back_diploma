package com.backenddiploma.dto.budget;

import com.backenddiploma.models.enums.BudgetType;
import com.backenddiploma.models.enums.Currency;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BudgetCreateDTO {

    private Long userId;
    private Long categoryId;
    private double plannedAmount;
    private Currency currency;
    private LocalDate periodStart;
}
