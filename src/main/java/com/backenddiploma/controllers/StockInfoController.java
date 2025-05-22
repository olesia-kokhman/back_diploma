package com.backenddiploma.controllers;

import com.backenddiploma.dto.stockinfo.StockInfoResponseDTO;
import com.backenddiploma.models.enums.StockTrend;
import com.backenddiploma.services.StockInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockInfoController {

    private final StockInfoService stockInfoService;

    @GetMapping
    public List<StockInfoResponseDTO> getAll() {
        return stockInfoService.getAllTrends();
    }

    @GetMapping("/{id}")
    public StockInfoResponseDTO getById(@PathVariable Long id) {
        return stockInfoService.getTrendById(id);
    }

    @GetMapping("/symbol/{symbol}")
    public StockInfoResponseDTO getBySymbol(@PathVariable String symbol) {
        return stockInfoService.getTrendByQuote(symbol);
    }

    @GetMapping("/gainers")
    public List<StockInfoResponseDTO> getGainers() {
        return stockInfoService.getByTrend(StockTrend.UP);
    }

    @GetMapping("/losers")
    public List<StockInfoResponseDTO> getLosers() {
        return stockInfoService.getByTrend(StockTrend.DOWN);
    }

    @GetMapping("/actives")
    public List<StockInfoResponseDTO> getActives() {
        return stockInfoService.getByTrend(StockTrend.ACTIVE);
    }

    @PostMapping("/sync")
    public void syncTrends() {
        stockInfoService.fetchAndSaveAll();
    }
}
