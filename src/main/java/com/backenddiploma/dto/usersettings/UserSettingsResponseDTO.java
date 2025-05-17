package com.backenddiploma.dto.usersettings;

import com.backenddiploma.models.enums.LanguageAbbreviation;
import com.backenddiploma.models.enums.Theme;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserSettingsResponseDTO {

    private Long id;
    private Long userId;
    private LanguageAbbreviation language;
    private String defaultCurrency;
    private String dateFormat;
    private String timeFormat;
    private Theme theme;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
