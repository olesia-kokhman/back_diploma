package com.backenddiploma.dto.transaction;

import com.backenddiploma.models.enums.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionResponseDTO {
    private Long id;
    private TransactionType transactionType;
    private double amount;
    private String currency;
    private String description;
    private Long accountId;
    private Long categoryId;
    private Long userId;
    private LocalDateTime transferredAt;
    private LocalDateTime dateAndTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
