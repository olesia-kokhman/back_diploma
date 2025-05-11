package com.backenddiploma.dto;

import com.backenddiploma.models.Currency;
import com.backenddiploma.models.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionRequestDTO {
    private TransactionType transactionType;
    private Long accountId;
    private double amount;
    private Currency currency;
    private Long categoryId;
    private String description;
    private LocalDateTime dateTime;
}
