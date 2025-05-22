package com.backenddiploma.config;

import com.backenddiploma.services.StockInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockSyncScheduler {

    private final StockInfoService stockInfoService;

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Kiev")
    public void syncStocksAtMidnight() {
        log.info("⏰ Starting scheduled stock sync at 00:00...");
        stockInfoService.fetchAndSaveAll();
        log.info("✅ Stock sync completed.");
    }
}
