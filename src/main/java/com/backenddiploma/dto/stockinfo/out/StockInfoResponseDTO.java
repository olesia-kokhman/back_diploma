package com.backenddiploma.dto.stockinfo.out;

import com.backenddiploma.models.enums.StockTrend;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockInfoResponseDTO {

    private String symbol;
    private String name;
    private double currentPrice;
    private double changePercent;
    private StockTrend trend;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
