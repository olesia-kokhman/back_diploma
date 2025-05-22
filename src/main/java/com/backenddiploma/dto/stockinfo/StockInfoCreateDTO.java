package com.backenddiploma.dto.stockinfo;

import lombok.Data;

@Data
public class StockInfoCreateDTO {
    private String symbol;
    private double currentPrice;     // c
    private double change;           // d
    private double percentChange;    // dp
    private double highPrice;        // h
    private double lowPrice;         // l
    private double openPrice;        // o
    private double prevClosePrice;   // pc
    private long timestamp;          // t
}
