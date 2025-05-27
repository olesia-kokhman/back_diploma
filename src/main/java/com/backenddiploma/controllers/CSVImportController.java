package com.backenddiploma.controllers;

import com.backenddiploma.services.CSVImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/importcsv")
public class CSVImportController {
    private final CSVImportService csvImportService;

    @PostMapping
    public ResponseEntity<String> importTransactions(@RequestParam("file") MultipartFile file, Long userId, Long accountId) {
        csvImportService.importCsv(file, userId, accountId);
        return ResponseEntity.ok("");
    }
}
