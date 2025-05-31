package com.backenddiploma.dto.integrations.monobank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonobankSyncAccountsTask {
    private String token;
    private Long userId;
    private String accountId;
}
