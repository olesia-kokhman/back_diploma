package com.backenddiploma.services.charts;

import com.backenddiploma.dto.integrations.monobank.MonobankExchangeRateDTO;
import com.backenddiploma.models.enums.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExchangeService {

    private final ExchangeCacheService cacheService;

    public Double convert(double amount, Currency from, Currency to) {
        if (from == to) {
            return amount;
        }

        try {
            List<MonobankExchangeRateDTO> rates = cacheService.getRates();
            if (rates == null) {
                return null;
            }

            Optional<MonobankExchangeRateDTO> rateOpt = findRate(rates, from.getIsoCode(), to.getIsoCode());

            if (rateOpt.isEmpty()) {
                return null;
            }

            MonobankExchangeRateDTO rate = rateOpt.get();
            double result;

            if (rate.getCurrencyCodeA() == from.getIsoCode()) {
                result = amount * rate.getRateBuy();
            } else {
                result = amount / rate.getRateSell();
            }

            return result;

        } catch (Exception exception) {
            return null;
        }
    }

    private Optional<MonobankExchangeRateDTO> findRate(List<MonobankExchangeRateDTO> rates, int codeA, int codeB) {

        Optional<MonobankExchangeRateDTO> directRate = rates.stream()
                .filter(rate -> rate.getCurrencyCodeA() == codeA && rate.getCurrencyCodeB() == codeB)
                .findFirst();

        if (directRate.isPresent()) {
            return directRate;
        }

        return rates.stream()
                .filter(rate -> rate.getCurrencyCodeA() == codeB && rate.getCurrencyCodeB() == codeA)
                .findFirst();
    }
}
