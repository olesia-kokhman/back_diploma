package com.backenddiploma.dto.transaction;

import com.backenddiploma.models.enums.Currency;
import com.backenddiploma.models.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionUpdateDTO {
    @NotNull(message = "Transaction type is required")
    private TransactionType transactionType;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotNull(message = "Currency is required")
    private Currency currency;

    @Size(max = 255, message = "Description must be at most 255 characters")
    private String description;

    @NotNull(message = "Account id is required")
    private Long accountId;

    @NotNull(message = "Category id is required")
    private Long categoryId;

    @NotNull(message = "Transferred date is required")
    @PastOrPresent(message = "Transferred date cannot be in the future")
    private LocalDateTime transferredAt;
}
