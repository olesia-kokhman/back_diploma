package com.backenddiploma.dto;

import lombok.Data;

@Data
public class StockWidgetDTO {
    private String symbol;
    private double currentPrice;     // c
    private double highPrice;        // h
    private double lowPrice;         // l
    private double openPrice;        // o
    private double prevClosePrice;   // pc
    private long timestamp;          // t
}
