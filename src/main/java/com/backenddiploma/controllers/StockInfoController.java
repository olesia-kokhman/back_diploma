package com.backenddiploma.controllers;

import com.backenddiploma.dto.stockinfo.out.StockInfoCreateDTO;
import com.backenddiploma.dto.stockinfo.out.StockInfoResponseDTO;
import com.backenddiploma.dto.stockinfo.out.StockInfoUpdateDTO;
import com.backenddiploma.services.StockInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockInfoController {

    private final StockInfoService stockInfoService;

    @PostMapping
    public ResponseEntity<StockInfoResponseDTO> create(@RequestBody StockInfoCreateDTO dto) {
        return ResponseEntity.ok(stockInfoService.create(dto));
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<StockInfoResponseDTO> getBySymbol(@PathVariable String symbol) {
        return ResponseEntity.ok(stockInfoService.getBySymbol(symbol));
    }

    @PutMapping("/{symbol}")
    public ResponseEntity<StockInfoResponseDTO> update(@PathVariable String symbol, @RequestBody StockInfoUpdateDTO dto) {
        return ResponseEntity.ok(stockInfoService.update(symbol, dto));
    }

    @DeleteMapping("/{symbol}")
    public ResponseEntity<Void> delete(@PathVariable String symbol) {
        stockInfoService.delete(symbol);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<StockInfoResponseDTO>> getAll() {
        return ResponseEntity.ok(stockInfoService.getAll());
    }
}
