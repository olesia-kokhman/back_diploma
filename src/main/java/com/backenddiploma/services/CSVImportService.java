package com.backenddiploma.services;

import com.backenddiploma.config.exceptions.NotFoundException;
import com.backenddiploma.dto.transaction.TransactionCSVImportDTO;
import com.backenddiploma.dto.transaction.TransactionCreateDTO;
import com.backenddiploma.mappers.CSVImportMapper;
import com.backenddiploma.models.Category;
import com.backenddiploma.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CSVImportService {

    private final CSVImportMapper mapper;
    private final TransactionService transactionService;
    final CategoryRepository categoryRepository;

    public void importCsv(MultipartFile file, Long userId, Long accountId) {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) {
            throw new IllegalArgumentException("Invalid file: must be .csv");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<TransactionCSVImportDTO> csvDtos = reader.lines()
                    .skip(1)
                    .map(mapper::parseLine)
                    .toList();

            for (TransactionCSVImportDTO csvDto : csvDtos) {
                Long transactionCategoryIfNull  = csvDto.getAmount() > 0 ? 29L : 28L;
//                Category category = categoryRepository
//                        .findByMccAndUserId(csvDto.getMcc(), userId)
//                        .orElseGet(() -> categoryRepository.findById(transactionCategoryIfNull)
//                                .orElseThrow(() -> new NotFoundException("User not found")));

                TransactionCreateDTO dto = new TransactionCreateDTO();
                dto.setTransactionType(csvDto.getTransactionType());
                dto.setAmount(csvDto.getAmount());
                dto.setCurrency(csvDto.getCurrency());
                dto.setDescription(csvDto.getDescription());
                dto.setTransferredAt(csvDto.getTransferredAt());
                dto.setUserId(userId);
                dto.setAccountId(accountId);
                dto.setCategoryId((long)1);
                transactionService.create(dto);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
    }
}
