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
            System.out.println("Currencies are the same (" + from + "). No conversion needed.");
            return amount;
        }

        try {
            List<MonobankExchangeRateDTO> rates = cacheService.getRates();
            if (rates == null) {
                System.out.println("Exchange rates are NULL! Cannot perform conversion.");
                return null;
            }

            Optional<MonobankExchangeRateDTO> rateOpt = findRate(rates, from.getIsoCode(), to.getIsoCode());

            if (rateOpt.isEmpty()) {
                System.out.println("NO exchange rate found from " + from + " to " + to + "!");
                return null;
            }

            MonobankExchangeRateDTO rate = rateOpt.get();

            double result;

            if (rate.getCurrencyCodeA() == from.getIsoCode()) {

                result = amount * rate.getRateBuy();
                System.out.println("Using DIRECT rate: " + rate.getRateBuy() + " → Result = " + result);
            } else {

                result = amount / rate.getRateSell();
                System.out.println("Using REVERSE rate: " + rate.getRateSell() + " → Result = " + result);
            }

            return result;

        } catch (Exception exception) {
            System.out.println("Exception during conversion: " + exception.getMessage());
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

        Optional<MonobankExchangeRateDTO> reverseRate = rates.stream()
                .filter(rate -> rate.getCurrencyCodeA() == codeB && rate.getCurrencyCodeB() == codeA)
                .findFirst();

        if (reverseRate.isPresent()) {
            System.out.println("Using REVERSE rate from " + codeB + " to " + codeA);
        }

        return reverseRate;
    }
}
