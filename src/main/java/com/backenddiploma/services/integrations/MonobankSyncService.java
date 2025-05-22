package com.backenddiploma.services.integrations;

import com.backenddiploma.dto.integrations.monobank.MonobankTransactionDTO;
import com.backenddiploma.dto.integrations.monobank.MonobankUserInfoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class MonobankSyncService {

    private final WebClient webClient;

    @Value("${monobank.api.url}")
    private String apiURL;

    public MonobankSyncService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.monobank.ua/").build();
    }

    public Mono<MonobankUserInfoDTO> getUserInfo(String token) {
        return webClient.get()
                .uri("https://api.monobank.ua/personal/client-info")
                .header("X-Token", token)
                .retrieve()
                .onStatus(status -> !status.equals(HttpStatus.OK), response -> {

                    return response.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new RuntimeException(
                                    "Error getting user info from Monobank API: HTTP status code: " + response.statusCode() +
                                            ", body response: " + errorBody)));
                })
                .bodyToMono(MonobankUserInfoDTO.class);
    }

    public Mono<List<MonobankTransactionDTO>> getTransactions(String token, String accountId, String from) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/personal/statement/{accountId}/{from}")
                        .build(accountId, from))
                .header("X-Token", token)
                .retrieve()
                .onStatus(status -> !status.equals(HttpStatus.OK), response -> {

                    return response.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new RuntimeException(
                                    "Error getting transactions from Monobank API: HTTP status code: " + response.statusCode() +
                                            ", body response: " + errorBody)));
                })
                .bodyToMono(new ParameterizedTypeReference<List<MonobankTransactionDTO>>() {});
    }
}
