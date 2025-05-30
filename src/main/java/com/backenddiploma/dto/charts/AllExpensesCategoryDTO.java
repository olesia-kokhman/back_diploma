package com.backenddiploma.dto.charts;

import lombok.Data;

@Data
public class AllExpensesCategoryDTO implements IChartDTO {
    private String categoryTitle;
    private String color;
    private String iconUrl;
    private double amount;
    private double percentage;
}
