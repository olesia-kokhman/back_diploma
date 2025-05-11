package com.backenddiploma.controllers;

import com.backenddiploma.dto.StockWidgetDTO;
import com.backenddiploma.services.StockWidgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockWidgetController {

    private final StockWidgetService stockWidgetService;

    @GetMapping("/quote")
    public ResponseEntity<StockWidgetDTO> getQuote(@RequestParam String symbol) {
        try {
            StockWidgetDTO quote = stockWidgetService.getQuoteBySymbol(symbol);
            return ResponseEntity.ok(quote);
        } catch (Exception exception) {
            return (ResponseEntity<StockWidgetDTO>) ResponseEntity.notFound();
        }
    }

    @GetMapping("/gainers")
    public ResponseEntity<List<StockWidgetDTO>> getGainers() {
        return ResponseEntity.ok(stockWidgetService.getGainers());
    }

    @GetMapping("/losers")
    public ResponseEntity<List<StockWidgetDTO>> getLosers() {
        return ResponseEntity.ok(stockWidgetService.getLosers());
    }

    @GetMapping("/actives")
    public ResponseEntity<List<StockWidgetDTO>> getActives() {
        return ResponseEntity.ok(stockWidgetService.getActives());
    }

}
