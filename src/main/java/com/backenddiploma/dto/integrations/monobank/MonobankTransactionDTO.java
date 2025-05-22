package com.backenddiploma.dto.integrations.monobank;

import lombok.Data;

@Data
public class MonobankTransactionDTO {
    private String id;
    private long time;
    private String description;
    private int mcc;
    private int originalMcc;
    private boolean hold;
    private long amount;
    private long operationAmount;
    private int currencyCode;
    private int commissionRate;
    private long cashbackAmount;
    private long balance;
    private String comment;
    private String receiptId;
    private String invoiceId;
    private String counterEdrpou;
    private String counterIban;
    private String counterName;
}
