package com.backenddiploma.dto.account;

import com.backenddiploma.models.enums.AccountType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AccountResponseDTO {
    private Long id;
    private String name;
    private AccountType accountType;
    private String externalAccountId;
    private String currency;
    private double balance;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Double quantity;
    private Double buyPrice;
    private LocalDate buyDate;
    private Double currentPrice;
    private String platform;

    private Double goal;

    private String lenderName;
    private Double initialAmount;
    private Double currentAmount;
    private Double interestRate;
    private LocalDate startDate;
    private LocalDate dueDate;
    private Boolean isRecurring;
}
