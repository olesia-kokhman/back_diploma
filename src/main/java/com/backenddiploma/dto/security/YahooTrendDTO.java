package com.backenddiploma.dto.security;

import lombok.Data;

import java.util.List;

@Data
public class YahooTrendDTO {

    private List<String> gainers;
    private List<String> losers;
    private List<String> actives;

}
