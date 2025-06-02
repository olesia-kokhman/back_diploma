package com.backenddiploma.unit.services;

import com.backenddiploma.dto.transaction.TransactionCSVImportDTO;
import com.backenddiploma.dto.transaction.TransactionCreateDTO;
import com.backenddiploma.mappers.CSVImportMapper;
import com.backenddiploma.models.Category;
import com.backenddiploma.models.enums.BudgetType;
import com.backenddiploma.models.enums.Currency;
import com.backenddiploma.models.enums.TransactionType;
import com.backenddiploma.repositories.CategoryRepository;
import com.backenddiploma.services.CSVImportService;
import com.backenddiploma.services.CategoryService;
import com.backenddiploma.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CSVImportServiceTest {

    @InjectMocks
    private CSVImportService csvImportService;

    @Mock
    private CSVImportMapper mapper;

    @Mock
    private TransactionService transactionService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryService categoryService;

    private MultipartFile mockMultipartFile;

    @BeforeEach
    void setup() {
        // Створимо валідний CSV файл (заголовок + одна транзакція)
        String csvContent = "transactionType,amount,currency,description,transferredAt,mcc\n" +
                "EXPENSE,-100.0,USD,Coffee,2024-06-01T12:00:00,5411\n";

        mockMultipartFile = new MultipartFile() {
            @Override
            public String getName() {
                return "file";
            }

            @Override
            public String getOriginalFilename() {
                return "test.csv";
            }

            @Override
            public String getContentType() {
                return "text/csv";
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return csvContent.length();
            }

            @Override
            public byte[] getBytes() throws IOException {
                return csvContent.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public ByteArrayInputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
            }

            @Override
            public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Test
    void whenImportCsv_validFile_thenTransactionsCreated() {
        // Arrange
        TransactionCSVImportDTO csvDto = new TransactionCSVImportDTO();
        csvDto.setTransactionType(TransactionType.EXPENSE);
        csvDto.setAmount(-100.0);
        csvDto.setCurrency(Currency.USD);
        csvDto.setDescription("Coffee");
        csvDto.setTransferredAt(LocalDateTime.of(2024, 6, 1, 12, 0));
        csvDto.setMcc(5411);

        when(mapper.parseLine(anyString())).thenReturn(csvDto);

        Category category = new Category();
        category.setId(10L);

        when(categoryService.getCategoryByMcc(5411, BudgetType.EXPENSE, 1L)).thenReturn(category);

        // Act
        csvImportService.importCsv(mockMultipartFile, 1L, 2L);

        // Assert
        ArgumentCaptor<TransactionCreateDTO> captor = ArgumentCaptor.forClass(TransactionCreateDTO.class);
        verify(transactionService, times(1)).create(captor.capture());

        TransactionCreateDTO createdDto = captor.getValue();
        assertEquals(TransactionType.EXPENSE, createdDto.getTransactionType());
        assertEquals(-100.0, createdDto.getAmount());
        assertEquals(Currency.USD, createdDto.getCurrency());
        assertEquals("Coffee", createdDto.getDescription());
        assertEquals(LocalDateTime.of(2024, 6, 1, 12, 0), createdDto.getTransferredAt());
        assertEquals(1L, createdDto.getUserId());
        assertEquals(2L, createdDto.getAccountId());
        assertEquals(10L, createdDto.getCategoryId());
    }

    @Test
    void whenImportCsv_notCsvFile_thenThrow() {
        MultipartFile invalidFile = mock(MultipartFile.class);
        when(invalidFile.isEmpty()).thenReturn(false);
        when(invalidFile.getOriginalFilename()).thenReturn("test.txt");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                csvImportService.importCsv(invalidFile, 1L, 2L)
        );

        assertTrue(ex.getMessage().contains("Invalid file"));
    }

    @Test
    void whenImportCsv_emptyFile_thenThrow() {
        MultipartFile emptyFile = mock(MultipartFile.class);
        when(emptyFile.isEmpty()).thenReturn(true);
        when(emptyFile.getOriginalFilename()).thenReturn("test.csv");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                csvImportService.importCsv(emptyFile, 1L, 2L)
        );

        assertTrue(ex.getMessage().contains("Invalid file"));
    }

    @Test
    void whenImportCsv_ioError_thenThrow() throws IOException {
        MultipartFile badFile = mock(MultipartFile.class);
        when(badFile.isEmpty()).thenReturn(false);
        when(badFile.getOriginalFilename()).thenReturn("test.csv");
        when(badFile.getInputStream()).thenThrow(new IOException("Boom!"));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                csvImportService.importCsv(badFile, 1L, 2L)
        );

        assertTrue(ex.getMessage().contains("Error reading CSV file"));
    }
}
