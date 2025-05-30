package com.backenddiploma.dto.transaction;

import com.backenddiploma.models.enums.Currency;
import com.backenddiploma.models.enums.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionResponseDTO {
    private Long id;
    private TransactionType transactionType;
    private double amount;
    private Currency currency;
    private String description;
    private Long accountId;
    private Long categoryId;
    private Long userId;
    private LocalDateTime transferredAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
