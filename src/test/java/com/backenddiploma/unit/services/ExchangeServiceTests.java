package com.backenddiploma.unit.services;

import com.backenddiploma.dto.integrations.monobank.MonobankExchangeRateDTO;
import com.backenddiploma.models.enums.Currency;
import com.backenddiploma.services.charts.ExchangeCacheService;
import com.backenddiploma.services.charts.ExchangeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeServiceTest {

    @InjectMocks
    private ExchangeService exchangeService;

    @Mock
    private ExchangeCacheService cacheService;

    // === CASE 1: from == to

    @Test
    void whenConvert_sameCurrency_thenReturnAmount() {
        double result = exchangeService.convert(100.0, Currency.UAH, Currency.UAH);

        assertEquals(100.0, result);
    }

    // === CASE 2: rates == null

    @Test
    void whenRatesNull_thenReturnNull() {
        when(cacheService.getRates()).thenReturn(null);

        Double result = exchangeService.convert(100.0, Currency.USD, Currency.UAH);

        assertNull(result);
    }

    // === CASE 3: no matching rate

    @Test
    void whenNoMatchingRate_thenReturnNull() {
        MonobankExchangeRateDTO rate = new MonobankExchangeRateDTO();
        rate.setCurrencyCodeA(Currency.EUR.getIsoCode());
        rate.setCurrencyCodeB(Currency.UAH.getIsoCode());
        rate.setRateBuy(40.0);
        rate.setRateSell(41.0);

        when(cacheService.getRates()).thenReturn(List.of(rate));

        Double result = exchangeService.convert(100.0, Currency.USD, Currency.UAH);

        assertNull(result);
    }

    // === CASE 4: direct rate

    @Test
    void whenDirectRateFound_thenConvertUsingRateBuy() {
        MonobankExchangeRateDTO rate = new MonobankExchangeRateDTO();
        rate.setCurrencyCodeA(Currency.USD.getIsoCode());
        rate.setCurrencyCodeB(Currency.UAH.getIsoCode());
        rate.setRateBuy(38.0);
        rate.setRateSell(39.0);

        when(cacheService.getRates()).thenReturn(List.of(rate));

        Double result = exchangeService.convert(10.0, Currency.USD, Currency.UAH);

        assertEquals(10.0 * 38.0, result);
    }

    // === CASE 5: reverse rate

    @Test
    void whenReverseRateFound_thenConvertUsingRateSell() {
        MonobankExchangeRateDTO rate = new MonobankExchangeRateDTO();
        rate.setCurrencyCodeA(Currency.UAH.getIsoCode());
        rate.setCurrencyCodeB(Currency.USD.getIsoCode());
        rate.setRateBuy(0.026);
        rate.setRateSell(39.5);

        when(cacheService.getRates()).thenReturn(List.of(rate));

        Double result = exchangeService.convert(3950.0, Currency.USD, Currency.UAH);

        // reverse → amount / rateSell
        assertEquals(3950.0 / 39.5, result);
    }

    // === CASE 6: exception in getRates

    @Test
    void whenExceptionOccurs_thenReturnNull() {
        when(cacheService.getRates()).thenThrow(new RuntimeException("Unexpected error"));

        Double result = exchangeService.convert(100.0, Currency.USD, Currency.UAH);

        assertNull(result);
    }
}
