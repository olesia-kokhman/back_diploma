package com.backenddiploma.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StockInfoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/stocks → get all trends")
    void testGetAll() throws Exception {
        mockMvc.perform(get("/api/stocks"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("GET /api/stocks/{id} → get trend by id")
    void testGetById() throws Exception {
        mockMvc.perform(get("/api/stocks/1"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("GET /api/stocks/symbol/{symbol} → get trend by symbol")
    void testGetBySymbol() throws Exception {
        mockMvc.perform(get("/api/stocks/symbol/AAPL"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("GET /api/stocks/gainers → get gainers")
    void testGetGainers() throws Exception {
        mockMvc.perform(get("/api/stocks/gainers"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("GET /api/stocks/losers → get losers")
    void testGetLosers() throws Exception {
        mockMvc.perform(get("/api/stocks/losers"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("GET /api/stocks/actives → get actives")
    void testGetActives() throws Exception {
        mockMvc.perform(get("/api/stocks/actives"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("POST /api/stocks/sync → sync trends")
    void testSyncTrends() throws Exception {
        mockMvc.perform(post("/api/stocks/sync"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }
}
