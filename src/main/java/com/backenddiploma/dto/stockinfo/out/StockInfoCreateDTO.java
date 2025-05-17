package com.backenddiploma.dto.stockinfo.out;

import com.backenddiploma.models.enums.StockTrend;
import lombok.Data;

@Data
public class StockInfoCreateDTO {

    private String symbol;
    private String name;
    private double currentPrice;
    private double changePercent;
    private StockTrend trend;
}
