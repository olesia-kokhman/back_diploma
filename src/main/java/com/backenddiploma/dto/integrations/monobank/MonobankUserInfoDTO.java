package com.backenddiploma.dto.integrations.monobank;

import lombok.Data;

import java.util.List;

@Data
public class MonobankUserInfoDTO {
    private String clientId;
    private String name;
    private String webHookUrl;
    private String permissions;
    private List<MonobankAccountDTO> accounts;
    private List<MonobankJarDTO> jars;
}
