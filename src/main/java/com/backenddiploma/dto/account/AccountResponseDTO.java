package com.backenddiploma.dto.account;

import com.backenddiploma.models.enums.AccountType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountResponseDTO {
    private Long id;
    private String name;
    private AccountType accountType;
    private String currency;
    private double balance;
    private boolean isMain;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
