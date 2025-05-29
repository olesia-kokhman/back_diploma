package com.backenddiploma.config.schedulers;

import com.backenddiploma.services.charts.ExchangeCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrencyRatesScheduler {

    private final ExchangeCacheService exchangeCacheService;

    @Scheduled(fixedRate = 5 * 60 * 1000)
    @CacheEvict(value = "exchangeRates", allEntries = true)
    public void clearExchangeCache() {
        System.out.println("Cleared exchangeRates cache");
        exchangeCacheService.getRates();
    }
}
