package com.backenddiploma.unit.mappers;

import com.backenddiploma.dto.usersettings.UserSettingsCreateDTO;
import com.backenddiploma.dto.usersettings.UserSettingsResponseDTO;
import com.backenddiploma.dto.usersettings.UserSettingsUpdateDTO;
import com.backenddiploma.mappers.UserSettingsMapper;
import com.backenddiploma.models.User;
import com.backenddiploma.models.UserSettings;
import com.backenddiploma.models.enums.Currency;
import com.backenddiploma.models.enums.LanguageAbbreviation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserSettingsMapperTest {

    private UserSettingsMapper userSettingsMapper;

    @BeforeEach
    void setUp() {
        userSettingsMapper = new UserSettingsMapper();
    }

    @Test
    void testToEntity() {
        UserSettingsCreateDTO dto = new UserSettingsCreateDTO();
        dto.setLanguage(LanguageAbbreviation.EN);
        dto.setDefaultCurrency("USD");
        dto.setDateFormat("yyyy-MM-dd");
        dto.setTimeFormat("HH:mm");

        User user = new User();
        user.setId(1L);

        UserSettings settings = userSettingsMapper.toEntity(dto, user);

        assertThat(settings.getUser()).isEqualTo(user);
        assertThat(settings.getLanguage()).isEqualTo("en");
        assertThat(settings.getDefaultCurrency()).isEqualTo(Currency.USD);
        assertThat(settings.getDateFormat()).isEqualTo("yyyy-MM-dd");
        assertThat(settings.getTimeFormat()).isEqualTo("HH:mm");
    }

    @Test
    void testUpdateUserSettingsFromDto() {
        UserSettings settings = new UserSettings();
        settings.setLanguage(LanguageAbbreviation.UK);
        settings.setDefaultCurrency(Currency.UAH);
        settings.setDateFormat("dd.MM.yyyy");
        settings.setTimeFormat("HH:mm:ss");

        UserSettingsUpdateDTO dto = new UserSettingsUpdateDTO();
        dto.setLanguage(LanguageAbbreviation.EN);
        dto.setDefaultCurrency("EUR");
        dto.setDateFormat("MM/dd/yyyy");
        dto.setTimeFormat("h:mm a");

        userSettingsMapper.updateUserSettingsFromDto(settings, dto);

        assertThat(settings.getLanguage()).isEqualTo("fr");
        assertThat(settings.getDefaultCurrency()).isEqualTo(Currency.EUR);
        assertThat(settings.getDateFormat()).isEqualTo("MM/dd/yyyy");
        assertThat(settings.getTimeFormat()).isEqualTo("h:mm a");
    }

    @Test
    void testToResponse() {
        UserSettings settings = new UserSettings();
        settings.setId(100L);

        User user = new User();
        user.setId(200L);
        settings.setUser(user);

        settings.setLanguage(LanguageAbbreviation.EN);
        settings.setDefaultCurrency(Currency.USD);
        settings.setDateFormat("yyyy-MM-dd");
        settings.setTimeFormat("HH:mm");

        settings.setCreatedAt(LocalDateTime.of(2025, 6, 1, 12, 0));
        settings.setUpdatedAt(LocalDateTime.of(2025, 6, 2, 15, 30));

        UserSettingsResponseDTO response = userSettingsMapper.toResponse(settings);

        assertThat(response.getId()).isEqualTo(100L);
        assertThat(response.getUserId()).isEqualTo(200L);
        assertThat(response.getLanguage()).isEqualTo("en");
        assertThat(response.getDefaultCurrency()).isEqualTo("USD");
        assertThat(response.getDateFormat()).isEqualTo("yyyy-MM-dd");
        assertThat(response.getTimeFormat()).isEqualTo("HH:mm");
        assertThat(response.getCreatedAt()).isEqualTo(LocalDateTime.of(2025, 6, 1, 12, 0));
        assertThat(response.getUpdatedAt()).isEqualTo(LocalDateTime.of(2025, 6, 2, 15, 30));
    }
}
