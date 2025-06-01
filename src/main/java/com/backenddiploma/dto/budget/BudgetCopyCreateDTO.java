package com.backenddiploma.dto.budget;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BudgetCopyCreateDTO {
    private Long userId;
    private LocalDate fromPeriodStart;
    private LocalDate toPeriodStart;
}
