package com.backenddiploma.config.schedulers;

import com.backenddiploma.dto.integrations.monobank.MonobankSyncAccountsTask;
import com.backenddiploma.services.MonobankService;
import com.backenddiploma.services.integrations.MonobankSyncAccountsQueue;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MonobankSyncAccountsScheduler {

    private final MonobankService monobankService;
    private final MonobankSyncAccountsQueue monobankSyncQueue;

    @Scheduled(fixedRate = 60_000)
    public void processQueue() {
        if (!monobankSyncQueue.isEmpty()) {
            MonobankSyncAccountsTask task = monobankSyncQueue.poll();

            if (task != null) {
                try {
                    System.out.println("Processing Monobank sync task for userId = " + task.getUserId() +
                            ", accountId = " + task.getAccountId());

                    monobankService.syncAccountTransactions(task.getToken(), task.getUserId(), task.getAccountId());

                    System.out.println("Finished Monobank sync task for userId = " + task.getUserId() +
                            ", accountId = " + task.getAccountId());
                } catch (Exception e) {
                    System.err.println("Error processing Monobank sync task for userId = " + task.getUserId() +
                            ", accountId = " + task.getAccountId() + ": " + e.getMessage());
                }
            }
        } else {
            System.out.println("Monobank sync queue is empty, waiting...");
        }
    }
}
