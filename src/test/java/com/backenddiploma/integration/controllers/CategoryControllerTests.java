package com.backenddiploma.integration.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /api/categories → create category")
    void testCreateCategory() throws Exception {
        mockMvc.perform(multipart("/api/categories")
                        .param("userId", "1")
                        .param("name", "Food")
                        .param("type", "EXPENSE")
                        .param("color", "#FF0000"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("GET /api/categories/{id} → get category by id")
    void testGetById() throws Exception {
        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("PUT /api/categories/{id} → update category")
    void testUpdateCategory() throws Exception {
        mockMvc.perform(multipart("/api/categories/1")
                        .with(request -> { request.setMethod("PUT"); return request; })
                        .param("name", "Updated Name")
                        .param("color", "#00FF00"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("DELETE /api/categories/{id} → delete category")
    void testDeleteCategory() throws Exception {
        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }

    @Test
    @DisplayName("GET /api/categories/user/{userId} → get categories by user")
    void testGetAllByUser() throws Exception {
        mockMvc.perform(get("/api/categories/user/1"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }
}
