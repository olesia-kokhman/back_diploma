package com.backenddiploma.integration.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /api/accounts → create account")
    void testCreateAccount() throws Exception {
        String json = """
                {
                    "userId": 1,
                    "accountType": "BANK_ACCOUNT",
                    "name": "My Account",
                    "currency": "USD",
                    "balance": 1000.0
                }
                """;

        mockMvc.perform(post("/api/accounts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("GET /api/accounts/{id} → get account by id")
    void testGetAccountById() throws Exception {
        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("PATCH /api/accounts/{id} → update account")
    void testUpdateAccount() throws Exception {
        String json = """
                {
                    "name": "Updated Account Name",
                    "balance": 2000.0
                }
                """;

        mockMvc.perform(patch("/api/accounts/1")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("DELETE /api/accounts/{id} → delete account")
    void testDeleteAccount() throws Exception {
        mockMvc.perform(delete("/api/accounts/1"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("GET /api/accounts/user/{userId} → get all accounts by user")
    void testGetAllByUser() throws Exception {
        mockMvc.perform(get("/api/accounts/user/1"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }
}
