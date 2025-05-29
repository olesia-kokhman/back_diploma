package com.backenddiploma.dto.charts;

import lombok.Data;

import java.util.List;

@Data
public class ChartDataResponseDTO {
    private String chartTitle;
    private List<IChartDTO> chartData;
    private double total;
}
