package com.backenddiploma.services;

import com.backenddiploma.config.exceptions.NotFoundException;
import com.backenddiploma.dto.stockinfo.StockInfoCreateDTO;
import com.backenddiploma.dto.stockinfo.StockInfoResponseDTO;
import com.backenddiploma.mappers.StockInfoMapper;
import com.backenddiploma.models.StockInfo;
import com.backenddiploma.models.enums.StockTrend;
import com.backenddiploma.repositories.StockInfoRepository;
import com.backenddiploma.services.integrations.StockWidgetService;
import com.backenddiploma.services.integrations.YahooTrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StockInfoService {
    private final YahooTrendService yahooTrendService;
    private final StockWidgetService stockWidgetService;
    private final StockInfoMapper mapper;
    private final StockInfoRepository repository;

    @Transactional()
    public void fetchAndSaveAll() {
        repository.deleteAll();
        Map<String, List<String>> trends = yahooTrendService.getTrends();
        createStockInfo(trends.getOrDefault("gainers", Collections.emptyList()), StockTrend.UP) ;
        createStockInfo(trends.getOrDefault("losers", Collections.emptyList()), StockTrend.DOWN) ;
        createStockInfo(trends.getOrDefault("actives", Collections.emptyList()), StockTrend.ACTIVE) ;
    }

    @Transactional()
    public void createStockInfo(List<String> trends, StockTrend trendType) { // internal request

        for(String trend: trends) {
            StockInfoCreateDTO dto = stockWidgetService.getQuoteBySymbol(trend);
            StockInfo stockInfo = mapper.toEntity(dto, trendType);
            repository.save(stockInfo);
        }
    }

    @Transactional(readOnly = true)
    public List<StockInfoResponseDTO> getAllTrends() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public StockInfoResponseDTO getTrendById(Long id) {
        return repository.findById((long) id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Stock trend not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public StockInfoResponseDTO getTrendByQuote(String symbol) {
        return repository.findBySymbol(symbol)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Stock trend not found for symbol: " + symbol));
    }

    @Transactional(readOnly = true)
    public List<StockInfoResponseDTO> getByTrend(StockTrend trend) {
        return repository.findByTrend(trend).stream()
                .map(mapper::toResponse)
                .toList();
    }
}
