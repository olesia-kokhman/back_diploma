package com.backenddiploma.mappers;

import com.backenddiploma.dto.transaction.TransactionCSVImportDTO;
import com.backenddiploma.models.enums.Currency;
import com.backenddiploma.models.enums.TransactionType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class CSVImportMapper {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public TransactionCSVImportDTO parseLine(String line) {
        String[] fields = line.split(",");

        if (fields.length != 6) {
            throw new IllegalArgumentException("Invalid CSV format. Expected 6 fields");
        }

        TransactionCSVImportDTO dto = new TransactionCSVImportDTO();
        dto.setTransactionType(TransactionType.valueOf(fields[0].trim().toUpperCase()));
        dto.setAmount(Double.parseDouble(fields[1].trim()));
        dto.setCurrency(Currency.valueOf(fields[2]));
        dto.setDescription(fields[3].trim());
        dto.setMcc(Integer.parseInt(fields[4].trim()));
        dto.setTransferredAt(LocalDateTime.parse(fields[5].trim(), DATE_FORMAT));

        return dto;
    }
}
