package com.backenddiploma.config.schedulers;

import com.backenddiploma.services.SavingTipService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RandomSavingTipScheduler {

    private final SavingTipService savingTipService;

    @Scheduled(cron = "0 0 * * * *", zone = "Europe/Kiev")
    public void updateTipHourly() {
        savingTipService.getRandomTip();
    }
}
