package com.backenddiploma.unit.services;

import com.backenddiploma.config.exceptions.NotFoundException;
import com.backenddiploma.dto.stockinfo.StockInfoCreateDTO;
import com.backenddiploma.dto.stockinfo.StockInfoResponseDTO;
import com.backenddiploma.mappers.StockInfoMapper;
import com.backenddiploma.models.StockInfo;
import com.backenddiploma.models.enums.StockTrend;
import com.backenddiploma.repositories.StockInfoRepository;
import com.backenddiploma.services.StockInfoService;
import com.backenddiploma.services.integrations.StockSyncService;
import com.backenddiploma.services.integrations.YahooSyncService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockInfoServiceTest {

    @InjectMocks
    private StockInfoService stockInfoService;

    @Mock
    private YahooSyncService yahooSyncService;

    @Mock
    private StockSyncService stockSyncService;

    @Mock
    private StockInfoMapper mapper;

    @Mock
    private StockInfoRepository repository;

    // === fetchAndSaveAll ===

    @Test
    void whenFetchAndSaveAll_thenSavesTrends() {
        Map<String, List<String>> trends = Map.of(
                "gainers", List.of("AAPL", "GOOG"),
                "losers", List.of("TSLA"),
                "actives", List.of("MSFT", "NFLX")
        );

        when(yahooSyncService.getTrends()).thenReturn(trends);
        when(stockSyncService.getQuoteBySymbol(anyString())).thenReturn(new StockInfoCreateDTO());
        when(mapper.toEntity(any(), any())).thenReturn(new StockInfo());

        stockInfoService.fetchAndSaveAll();

        verify(repository).deleteAll();
        verify(yahooSyncService).getTrends();
        verify(stockSyncService, times(5)).getQuoteBySymbol(anyString());
        verify(repository, times(5)).save(any(StockInfo.class));
    }

    @Test
    void whenFetchAndSaveAll_withEmptyTrends_thenOnlyDeleteAll() {
        when(yahooSyncService.getTrends()).thenReturn(Collections.emptyMap());

        stockInfoService.fetchAndSaveAll();

        verify(repository).deleteAll();
        verify(yahooSyncService).getTrends();
        verifyNoMoreInteractions(stockSyncService, mapper, repository);
    }

    // === getAllTrends ===

    @Test
    void whenGetAllTrends_thenReturnMappedList() {
        StockInfo stock1 = new StockInfo();
        StockInfo stock2 = new StockInfo();

        when(repository.findAll()).thenReturn(List.of(stock1, stock2));
        when(mapper.toResponse(stock1)).thenReturn(new StockInfoResponseDTO());
        when(mapper.toResponse(stock2)).thenReturn(new StockInfoResponseDTO());

        List<StockInfoResponseDTO> result = stockInfoService.getAllTrends();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // === getTrendById ===

    @Test
    void whenGetTrendById_thenReturnMappedTrend() {
        StockInfo stock = new StockInfo();

        when(repository.findById(1L)).thenReturn(Optional.of(stock));
        when(mapper.toResponse(stock)).thenReturn(new StockInfoResponseDTO());

        StockInfoResponseDTO result = stockInfoService.getTrendById(1L);

        assertNotNull(result);
    }

    @Test
    void whenGetTrendById_notFound_thenThrow() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> stockInfoService.getTrendById(1L));
    }

    // === getTrendByQuote ===

    @Test
    void whenGetTrendByQuote_thenReturnMappedTrend() {
        StockInfo stock = new StockInfo();

        when(repository.findBySymbol("AAPL")).thenReturn(Optional.of(stock));
        when(mapper.toResponse(stock)).thenReturn(new StockInfoResponseDTO());

        StockInfoResponseDTO result = stockInfoService.getTrendByQuote("AAPL");

        assertNotNull(result);
    }

    @Test
    void whenGetTrendByQuote_notFound_thenThrow() {
        when(repository.findBySymbol("AAPL")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> stockInfoService.getTrendByQuote("AAPL"));
    }

    // === getByTrend ===

    @Test
    void whenGetByTrend_thenReturnMappedList() {
        StockInfo stock1 = new StockInfo();
        StockInfo stock2 = new StockInfo();

        when(repository.findByTrend(StockTrend.UP)).thenReturn(List.of(stock1, stock2));
        when(mapper.toResponse(stock1)).thenReturn(new StockInfoResponseDTO());
        when(mapper.toResponse(stock2)).thenReturn(new StockInfoResponseDTO());

        List<StockInfoResponseDTO> result = stockInfoService.getByTrend(StockTrend.UP);

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
