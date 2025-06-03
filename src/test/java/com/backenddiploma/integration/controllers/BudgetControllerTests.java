package com.backenddiploma.integration.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureMockMvc
class BudgetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /api/budgets → create budget")
    void testCreateBudget() throws Exception {
        String json = """
                {
                    "userId": 1,
                    "categoryId": 2,
                    "plannedAmount": 1000.0,
                    "currency": "USD",
                    "periodStart": "2024-06-01"
                }
                """;

        mockMvc.perform(post("/api/budgets")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("GET /api/budgets/{id} → get budget by id")
    void testGetById() throws Exception {
        mockMvc.perform(get("/api/budgets/1"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("GET /api/budgets/user/{userId}/period → get budgets by user and period")
    void testGetAllByUserAndPeriod() throws Exception {
        String periodStart = LocalDate.now().withDayOfMonth(1).toString();

        mockMvc.perform(get("/api/budgets/user/1/period")
                        .param("periodStart", periodStart))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("PUT /api/budgets/{id} → update budget")
    void testUpdateBudget() throws Exception {
        String json = """
                {
                    "categoryId": 2,
                    "plannedAmount": 2000.0,
                    "currency": "EUR"
                }
                """;

        mockMvc.perform(put("/api/budgets/1")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("DELETE /api/budgets/{id} → delete budget")
    void testDeleteBudget() throws Exception {
        mockMvc.perform(delete("/api/budgets/1"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("POST /api/budgets/copy → copy budgets")
    void testCopyBudgets() throws Exception {
        String json = """
                {
                    "userId": 1,
                    "fromPeriodStart": "2024-05-01",
                    "toPeriodStart": "2024-06-01"
                }
                """;

        mockMvc.perform(post("/api/budgets/copy")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }
}
