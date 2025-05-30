package com.backenddiploma.dto.account;

import com.backenddiploma.models.enums.AccountType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AccountUpdateDTO {
    private String name;
    private AccountType accountType;
    private String currency;
    private Double balance;
    private Boolean isMain;

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
