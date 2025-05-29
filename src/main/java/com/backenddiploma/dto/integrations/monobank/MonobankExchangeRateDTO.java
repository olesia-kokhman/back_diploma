package com.backenddiploma.dto.integrations.monobank;

import lombok.Data;

@Data
public class MonobankExchangeRateDTO {
    private int currencyCodeA;
    private int currencyCodeB;
    private long date;
    private double rateSell;
    private double rateBuy;
    private double rateCross;
}
