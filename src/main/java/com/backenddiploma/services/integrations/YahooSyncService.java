package com.backenddiploma.services.integrations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@RequiredArgsConstructor
public class YahooSyncService {

    private final ObjectMapper objectMapper;
    private WebClient webClient;

    @Value("${yahoo.api.url}")
    private String apiUrl;

    @Value("${yahoo.api.key}")
    private String apiKey;

    @Value("${yahoo.api.host}")
    private String apiHost;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("X-RapidAPI-Key", apiKey)
                .defaultHeader("X-RapidAPI-Host", apiHost)
                .build();
    }

    public Map<String, List<String>> getTrends() {
        try {
            String response = webClient.get()
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (response == null || response.isEmpty()) {
                throw new RuntimeException("Empty response from Yahoo API");
            }

            JsonNode root = objectMapper.readTree(response);
            JsonNode results = root.path("finance").path("result");

            Map<String, List<String>> trendMap = new HashMap<>();

            for (JsonNode group : results) {
                String title = group.path("title").asText();
                JsonNode quotes = group.path("quotes");

                List<String> symbols = new ArrayList<>();
                for (JsonNode quote : quotes) {
                    symbols.add(quote.path("symbol").asText());
                }

                switch (title) {
                    case "Day Gainers" -> trendMap.put("gainers", symbols);
                    case "Day Losers" -> trendMap.put("losers", symbols);
                    case "Most Actives" -> trendMap.put("actives", symbols);
                }
            }

            return trendMap;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Yahoo trends via WebClient", e);
        }
    }
}
