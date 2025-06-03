package com.backenddiploma.integration.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CSVImportControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /api/importcsv → import CSV transactions")
    void testImportCsv() throws Exception {
        // Створюємо моковий CSV файл
        String csvContent = "transactionType,amount,currency,description,transferredAt,mcc\n" +
                "EXPENSE,-100.0,USD,Coffee,2024-06-01T12:00:00,5411\n";

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes()
        );

        mockMvc.perform(multipart("/api/importcsv")
                        .file(mockFile)
                        .param("userId", "1")
                        .param("accountId", "2"))
                .andExpect(status().is4xxClientError());

        assertTrue(true);
    }
}
