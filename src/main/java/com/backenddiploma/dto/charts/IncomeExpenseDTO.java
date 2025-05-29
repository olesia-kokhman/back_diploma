package com.backenddiploma.dto.charts;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IncomeExpenseDTO implements IChartDTO{
    private String month;
    private double expense;
    private double income;
}

