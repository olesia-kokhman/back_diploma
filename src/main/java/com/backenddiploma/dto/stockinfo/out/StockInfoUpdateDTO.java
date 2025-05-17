package com.backenddiploma.dto.stockinfo.out;

import com.backenddiploma.models.enums.StockTrend;
import lombok.Data;

@Data
public class StockInfoUpdateDTO {

    private String name;
    private Double currentPrice;
    private Double changePercent;
    private StockTrend trend;
}
