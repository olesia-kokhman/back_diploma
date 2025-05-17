package com.backenddiploma.dto.account;

import com.backenddiploma.models.enums.AccountType;
import lombok.Data;

@Data
public class AccountUpdateDTO {
    private String name;
    private AccountType accountType;
    private String currency;
    private Double balance;
    private Boolean isMain;
}
