package com.backenddiploma.services;

import com.backenddiploma.dto.security.StockWidgetDTO;
import com.backenddiploma.dto.security.YahooTrendDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockWidgetService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final YahooTrendService yahooTrendService;

    @Value("${finnhub.api.key}")
    private String apiKey;

    @Value("${finnhub.api.url}")
    private String apiUrl;

    @Getter
    private List<StockWidgetDTO> gainers = new ArrayList<>();
    @Getter
    private List<StockWidgetDTO> losers = new ArrayList<>();
    @Getter
    private List<StockWidgetDTO> actives = new ArrayList<>();

    @PostConstruct
    public void refreshTrends() {
        //YahooTrendDTO trends = yahooTrendService.getTrends();

        YahooTrendDTO trends = new YahooTrendDTO();

        trends.setGainers(List.of("ANGI", "OSCR", "LIVN", "ELAN", "AVDX", "CRL"));
        trends.setLosers(List.of("LNTH", "SRPT", "LFST", "PAYO", "UPST", "MRVL"));
        trends.setActives(List.of("LCID", "GOOGL", "NVDA", "AVDX", "PLTR", "AMD"));

        this.gainers = trends.getGainers().stream()
                .map(this::getQuoteBySymbol)
                .collect(Collectors.toList());

        this.losers = trends.getLosers().stream()
                .map(this::getQuoteBySymbol)
                .collect(Collectors.toList());

        this.actives = trends.getActives().stream()
                .map(this::getQuoteBySymbol)
                .collect(Collectors.toList());
    }


    public StockWidgetDTO getQuoteBySymbol(String symbol) {
        String url = String.format("%s?symbol=%s&token=%s", apiUrl, symbol, apiKey);
        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode json = objectMapper.readTree(response);

            StockWidgetDTO dto = new StockWidgetDTO();
            dto.setSymbol(symbol);
            dto.setCurrentPrice(json.path("c").asDouble());
            dto.setHighPrice(json.path("h").asDouble());
            dto.setLowPrice(json.path("l").asDouble());
            dto.setOpenPrice(json.path("o").asDouble());
            dto.setPrevClosePrice(json.path("pc").asDouble());
            dto.setTimestamp(json.path("t").asLong());

            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch stock quote from Finnhub", e);
        }
    }
}
