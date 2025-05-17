package com.backenddiploma.dto.transaction;

import com.backenddiploma.models.enums.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionUpdateDTO {
    private TransactionType transactionType;
    private Double amount;
    private String currency;
    private String description;
    private Long accountId;
    private Long categoryId;
    private LocalDateTime transferredAt;
    private LocalDateTime dateAndTime;
}
