package com.backenddiploma.services.charts;

import com.backenddiploma.dto.integrations.monobank.MonobankExchangeRateDTO;
import com.backenddiploma.models.enums.Currency;
import com.backenddiploma.services.integrations.MonobankSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExchangeService {

    private final ExchangeCacheService cacheService;

    public Double convert(double amount, Currency from, Currency to) {
        if (from == to) return amount;

        try {
            List<MonobankExchangeRateDTO> rates = cacheService.getRates();
            if (rates == null) return null;

            return findRate(rates, from.getIsoCode(), to.getIsoCode())
                    .map(rate -> amount * rate.getRateBuy())
                    .orElse(null);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    private Optional<MonobankExchangeRateDTO> findRate(List<MonobankExchangeRateDTO> rates, int codeA, int codeB) {
        return rates.stream()
                .filter(rate -> rate.getCurrencyCodeA() == codeA && rate.getCurrencyCodeB() == codeB)
                .findFirst();
    }
}
