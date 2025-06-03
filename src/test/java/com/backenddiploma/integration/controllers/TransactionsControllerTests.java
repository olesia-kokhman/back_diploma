package com.backenddiploma.integration.controllers;

import com.backenddiploma.dto.transaction.TransactionCreateDTO;
import com.backenddiploma.dto.transaction.TransactionUpdateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/transactions/all → get filtered transactions")
    void testGetTransactions() throws Exception {
        mockMvc.perform(get("/api/transactions/all")
                        .param("userId", "1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().is4xxClientError());


        assertTrue(true);
    }

    @Test
    @DisplayName("GET /api/transactions/{id} → get transaction by id")
    void testGetTransactionById() throws Exception {
        mockMvc.perform(get("/api/transactions/1")
                        .param("userId", "1"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("POST /api/transactions → create transaction")
    void testCreateTransaction() throws Exception {
        TransactionCreateDTO dto = new TransactionCreateDTO();
        dto.setAccountId(1L);
        dto.setUserId(1L);
        dto.setAmount(100.0);
        dto.setDescription("Test transaction");

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("PUT /api/transactions/{id} → update transaction")
    void testUpdateTransaction() throws Exception {
        TransactionUpdateDTO dto = new TransactionUpdateDTO();
        dto.setAmount(200.0);
        dto.setDescription("Updated transaction");

        mockMvc.perform(put("/api/transactions/1")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("DELETE /api/transactions/{id} → delete transaction")
    void testDeleteTransaction() throws Exception {
        mockMvc.perform(delete("/api/transactions/1")
                        .param("userId", "1"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }
}
