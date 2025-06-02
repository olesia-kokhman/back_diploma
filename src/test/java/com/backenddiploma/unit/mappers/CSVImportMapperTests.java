package com.backenddiploma.unit.mappers;

import com.backenddiploma.dto.transaction.TransactionCSVImportDTO;
import com.backenddiploma.mappers.CSVImportMapper;
import com.backenddiploma.models.enums.Currency;
import com.backenddiploma.models.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CSVImportMapperTest {

    private CSVImportMapper csvImportMapper;

    @BeforeEach
    void setUp() {
        csvImportMapper = new CSVImportMapper();
    }

    @Test
    void testParseLine_validLine() {
        String csvLine = "EXPENSE,1234.56,USD,Groceries,5411,2025-06-01T12:30:45";

        TransactionCSVImportDTO dto = csvImportMapper.parseLine(csvLine);

        assertThat(dto.getTransactionType()).isEqualTo(TransactionType.EXPENSE);
        assertThat(dto.getAmount()).isEqualTo(1234.56);
        assertThat(dto.getCurrency()).isEqualTo(Currency.USD);
        assertThat(dto.getDescription()).isEqualTo("Groceries");
        assertThat(dto.getMcc()).isEqualTo(5411);
        assertThat(dto.getTransferredAt()).isEqualTo(LocalDateTime.of(2025, 6, 1, 12, 30, 45));
    }

    @Test
    void testParseLine_invalidFieldCount() {
        // Only 5 fields instead of 6
        String invalidCsvLine = "EXPENSE,100,USD,Groceries,5411";

        assertThatThrownBy(() -> csvImportMapper.parseLine(invalidCsvLine))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid CSV format");
    }

    @Test
    void testParseLine_invalidTransactionType() {
        String invalidCsvLine = "INVALID_TYPE,100,USD,Groceries,5411,2025-06-01T12:30:45";

        assertThatThrownBy(() -> csvImportMapper.parseLine(invalidCsvLine))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testParseLine_invalidAmount() {
        String invalidCsvLine = "EXPENSE,notANumber,USD,Groceries,5411,2025-06-01T12:30:45";

        assertThatThrownBy(() -> csvImportMapper.parseLine(invalidCsvLine))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    void testParseLine_invalidDateFormat() {
        String invalidCsvLine = "EXPENSE,100,USD,Groceries,5411,01-06-2025 12:30:45";

        assertThatThrownBy(() -> csvImportMapper.parseLine(invalidCsvLine))
                .isInstanceOf(java.time.format.DateTimeParseException.class);
    }
}
