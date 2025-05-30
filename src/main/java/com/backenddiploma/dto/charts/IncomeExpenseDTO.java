package com.backenddiploma.dto.charts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
public class IncomeExpenseDTO implements IChartDTO {
    private String month;
    private double expense;
    private double income;
    private Map<String, Double> incomeByCategory;
    private Map<String, Double> expenseByCategory;
}

