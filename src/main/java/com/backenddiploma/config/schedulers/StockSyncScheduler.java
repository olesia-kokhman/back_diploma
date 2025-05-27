package com.backenddiploma.config.schedulers;

import com.backenddiploma.services.StockInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockSyncScheduler {

    private final StockInfoService stockInfoService;

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Kiev")
    public void syncStocksAtMidnight() {
        stockInfoService.fetchAndSaveAll();
    }
}
