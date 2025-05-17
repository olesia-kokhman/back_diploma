package com.backenddiploma.dto.account;

import com.backenddiploma.models.enums.AccountType;
import lombok.Data;

@Data
public class AccountCreateDTO {
    private String name;
    private AccountType accountType;
    private String currency;
    private double balance;
    private boolean isMain;
    private Long userId;
}
