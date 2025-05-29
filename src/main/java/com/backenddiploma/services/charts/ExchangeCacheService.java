package com.backenddiploma.services.charts;

import com.backenddiploma.dto.integrations.monobank.MonobankExchangeRateDTO;
import com.backenddiploma.services.integrations.MonobankSyncService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExchangeCacheService {
    private final MonobankSyncService monobankSyncService;

    public ExchangeCacheService(MonobankSyncService monobankSyncService) {
        this.monobankSyncService = monobankSyncService;
    }

    @Cacheable("exchangeRates")
    public List<MonobankExchangeRateDTO> getRates() {
        System.out.println("Fetching from Monobank...");
        return monobankSyncService.getCurrencyRates().block();
    }
}

