package com.backenddiploma.controllers;

import com.backenddiploma.dto.YahooTrendDTO;
import com.backenddiploma.services.YahooTrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trends")
@RequiredArgsConstructor
public class YahooTrendController {

    private final YahooTrendService yahooTrendService;

    @GetMapping()
    public ResponseEntity<YahooTrendDTO> getStockTrends() {
        YahooTrendDTO trends = yahooTrendService.getTrends();
        return ResponseEntity.ok(trends);
    }
}
