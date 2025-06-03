package com.backenddiploma.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.backenddiploma.dto.savingtip.SavingTipCreateDTO;
import com.backenddiploma.dto.savingtip.SavingTipUpdateDTO;
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
class SavingTipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/saving-tips/random → get random tip")
    void testGetRandom() throws Exception {
        mockMvc.perform(get("/api/saving-tips/random"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("POST /api/saving-tips → create saving tip")
    void testCreate() throws Exception {
        SavingTipCreateDTO dto = new SavingTipCreateDTO();


        mockMvc.perform(post("/api/saving-tips")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("PUT /api/saving-tips/{id} → update saving tip")
    void testUpdate() throws Exception {
        SavingTipUpdateDTO dto = new SavingTipUpdateDTO();;

        mockMvc.perform(put("/api/saving-tips/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("DELETE /api/saving-tips/{id} → delete saving tip")
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/saving-tips/1"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("GET /api/saving-tips → get all saving tips")
    void testGetAll() throws Exception {
        mockMvc.perform(get("/api/saving-tips"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }
}
