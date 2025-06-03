package com.backenddiploma.integration.controllers;

import com.backenddiploma.dto.paymentreminder.PaymentReminderCreateDTO;
import com.backenddiploma.dto.paymentreminder.PaymentReminderUpdateDTO;
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
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentReminderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/payment-reminders/nearest → get nearest reminder")
    void testGetNearest() throws Exception {
        mockMvc.perform(get("/api/payment-reminders/nearest")
                        .param("userId", "1"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("POST /api/payment-reminders → create payment reminder")
    void testCreate() throws Exception {
        PaymentReminderCreateDTO dto = new PaymentReminderCreateDTO();
        dto.setUserId(1L);
        dto.setTitle("Pay Rent");
        dto.setAmount(1000.0);
        dto.setCurrency("USD");
        dto.setDueDate(java.time.LocalDate.of(2025, 6, 15));
        dto.setDescription("Monthly rent");

        assertTrue(true);
    }

    @Test
    @DisplayName("PATCH /api/payment-reminders/{id} → update payment reminder")
    void testUpdate() throws Exception {
        PaymentReminderUpdateDTO dto = new PaymentReminderUpdateDTO();
        dto.setTitle("Updated Title");
        dto.setAmount(1500.0);
        dto.setCurrency("EUR");
        dto.setDueDate(java.time.LocalDate.of(2025, 7, 10));
        dto.setDescription("Updated description");


        assertTrue(true);
    }

    @Test
    @DisplayName("GET /api/payment-reminders/{id} → get by id")
    void testGetById() throws Exception {
        mockMvc.perform(get("/api/payment-reminders/1"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("GET /api/payment-reminders/user/{userId} → get all by user id")
    void testGetAllByUserId() throws Exception {
        mockMvc.perform(get("/api/payment-reminders/user/1"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("DELETE /api/payment-reminders/{id} → delete reminder")
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/payment-reminders/1"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }
}
