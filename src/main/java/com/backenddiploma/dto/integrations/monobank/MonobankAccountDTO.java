package com.backenddiploma.dto.integrations.monobank;

import com.backenddiploma.dto.integrations.monobank.enums.CashbackType;
import com.backenddiploma.dto.integrations.monobank.enums.MonobankAccountType;
import lombok.Data;

import java.util.List;

@Data
public class MonobankAccountDTO {
    private String id;
    private String sendId;
    private int balance;
    private int creditLimit;
    private MonobankAccountType type;
    private int currencyCode;
    private CashbackType cashbackType;
    private List<String> maskedPan;
    private String iban;
}
