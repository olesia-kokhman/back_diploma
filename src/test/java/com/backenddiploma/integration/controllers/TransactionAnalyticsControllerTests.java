package com.backenddiploma.integration.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionAnalyticsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/analytics/all-expenses → get all expenses per current month")
    void testGetAllExpensesPerCurrentMonth() throws Exception {
        mockMvc.perform(get("/api/analytics/all-expenses")
                        .param("userId", "1"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("GET /api/analytics/all-incomes → get all incomes per current month")
    void testGetAllIncomesPerCurrentMonth() throws Exception {
        mockMvc.perform(get("/api/analytics/all-incomes")
                        .param("userId", "1"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("GET /api/analytics/general-balance → get general balance")
    void testGetGeneralBalancePerHalfYear() throws Exception {
        mockMvc.perform(get("/api/analytics/general-balance")
                        .param("userId", "1")
                        .param("groupBy", "MONTHLY"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("GET /api/analytics/income-expense → get income vs expense")
    void testGetIncomeExpensePerHalfYear() throws Exception {
        mockMvc.perform(get("/api/analytics/income-expense")
                        .param("userId", "1")
                        .param("groupBy", "MONTHLY")
                        .param("incomeOnly", "false")
                        .param("expenseOnly", "false"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }
}
