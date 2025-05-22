package com.backenddiploma.dto.integrations.monobank;

import lombok.Data;

@Data
public class MonobankJarDTO {
    private String id;
    private String sendId;
    private String title;
    private String description;
    private int currencyCode;
    private int balance;
    private int goal;
}
