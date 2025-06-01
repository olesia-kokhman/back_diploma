package com.backenddiploma.dto.budget;

import com.backenddiploma.models.enums.BudgetType;
import com.backenddiploma.models.enums.Currency;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BudgetUpdateDTO {

    private Long categoryId;
    private BudgetType type;
    private Double plannedAmount;
    private Currency currency;
}
