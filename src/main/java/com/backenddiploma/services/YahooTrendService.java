package com.backenddiploma.services;

import com.backenddiploma.dto.stockinfo.in.YahooTrendDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YahooTrendService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${yahoo.api.url}")
    private String apiUrl;

    @Value("${yahoo.api.key}")
    private String apiKey;

    @Value("${yahoo.api.host}")
    private String apiHost;

    public YahooTrendDTO getTrends() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", apiKey);
        headers.set("X-RapidAPI-Host", apiHost);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                entity,
                String.class
        );

        if (response.getBody() == null || response.getBody().isEmpty()) {
            throw new RuntimeException("Empty response from Yahoo API");
        }

        YahooTrendDTO trendsDTO = new YahooTrendDTO();
        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode results = root.path("finance").path("result");

            for (JsonNode group : results) {
                String title = group.path("title").asText();
                JsonNode quotes = group.path("quotes");

                List<String> symbols = new ArrayList<>();
                for (JsonNode quote : quotes) {
                    symbols.add(quote.path("symbol").asText());
                }

                switch (title) {
                    case "Day Gainers" -> trendsDTO.setGainers(symbols);
                    case "Day Losers" -> trendsDTO.setLosers(symbols);
                    case "Most Actives" -> trendsDTO.setActives(symbols);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Yahoo trends JSON", e);
        }

        return trendsDTO;
    }

}
