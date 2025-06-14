package com.backenddiploma.dto.budget;

import com.backenddiploma.models.enums.BudgetType;
import com.backenddiploma.models.enums.Currency;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BudgetResponseDTO {

    private Long id;
    private Long userId;
    private Long categoryId;
    private double plannedAmount;
    private double actualAmount;
    private double availableAmount;
    private Currency currency;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
