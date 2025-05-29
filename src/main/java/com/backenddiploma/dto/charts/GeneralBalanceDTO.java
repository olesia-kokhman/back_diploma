package com.backenddiploma.dto.charts;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeneralBalanceDTO implements IChartDTO {
    private String month;
    private double amount;
}
