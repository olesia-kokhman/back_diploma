package com.backenddiploma.dto.savingtip;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SavingTipResponseDTO {
    private Long id;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
