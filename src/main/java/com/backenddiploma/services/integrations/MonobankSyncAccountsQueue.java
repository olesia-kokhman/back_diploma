package com.backenddiploma.services.integrations;

import com.backenddiploma.dto.integrations.monobank.MonobankSyncAccountsTask;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class MonobankSyncAccountsQueue {

    private final Queue<MonobankSyncAccountsTask> queue = new ConcurrentLinkedQueue<>();

    public void enqueue(String token, Long userId, String accountId) {
        MonobankSyncAccountsTask task = new MonobankSyncAccountsTask(token, userId, accountId);
        queue.offer(task);
        System.out.println("Enqueued Monobank sync task for userId = " + userId + ", accountId = " + accountId);
    }

    public MonobankSyncAccountsTask poll() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
