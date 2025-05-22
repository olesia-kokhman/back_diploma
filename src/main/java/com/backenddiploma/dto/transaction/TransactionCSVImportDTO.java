package com.backenddiploma.dto.transaction;

import com.backenddiploma.models.enums.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionCSVImportDTO {
    private TransactionType transactionType;
    private double amount;
    private String currency;
    private String description;
    private int mcc;
    private LocalDateTime transferredAt;
}
