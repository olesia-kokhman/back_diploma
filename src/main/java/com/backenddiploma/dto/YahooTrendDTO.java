package com.backenddiploma.dto;

import lombok.Data;

import java.util.List;

@Data
public class YahooTrendDTO {

    private List<String> gainers;
    private List<String> losers;
    private List<String> actives;

}
