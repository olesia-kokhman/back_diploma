package com.backenddiploma.dto.account;

import com.backenddiploma.models.enums.AccountType;
import com.backenddiploma.models.enums.Currency;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AccountCreateDTO {
    private String name;
    private AccountType accountType;
    private Currency currency;
    private String externalAccountId;
    private double balance;
    private boolean isMain;
    private Long userId;

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
    private boolean isRecurring;


}
