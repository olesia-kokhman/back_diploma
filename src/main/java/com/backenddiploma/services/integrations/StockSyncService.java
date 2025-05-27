package com.backenddiploma.services.integrations;

import com.backenddiploma.dto.stockinfo.StockInfoCreateDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class StockSyncService {

    private final ObjectMapper objectMapper;
    private WebClient webClient;

    @Value("${finnhub.api.key}")
    private String apiKey;

    @Value("${finnhub.api.url}")
    private String apiUrl;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .build();
    }

    public StockInfoCreateDTO getQuoteBySymbol(String symbol) {
        String url = String.format("?symbol=%s&token=%s", symbol, apiKey);

        try {
            String response = webClient
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode json = objectMapper.readTree(response);

            StockInfoCreateDTO dto = new StockInfoCreateDTO();
            dto.setSymbol(symbol);
            dto.setCurrentPrice(json.path("c").asDouble());
            dto.setPercentChange(json.path("dp").asDouble());
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
