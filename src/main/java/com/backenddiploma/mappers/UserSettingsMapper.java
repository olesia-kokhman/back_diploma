package com.backenddiploma.mappers;

import com.backenddiploma.dto.usersettings.UserSettingsCreateDTO;
import com.backenddiploma.dto.usersettings.UserSettingsResponseDTO;
import com.backenddiploma.dto.usersettings.UserSettingsUpdateDTO;
import com.backenddiploma.models.User;
import com.backenddiploma.models.UserSettings;
import org.springframework.stereotype.Component;

import com.backenddiploma.models.enums.Currency;
@Component
public class UserSettingsMapper {

    public UserSettings toEntity(UserSettingsCreateDTO dto, User user) {
        UserSettings settings = new UserSettings();
        settings.setUser(user);
        settings.setLanguage(dto.getLanguage());
        settings.setDefaultCurrency(Currency.valueOf(dto.getDefaultCurrency()));
        settings.setDateFormat(dto.getDateFormat());
        settings.setTimeFormat(dto.getTimeFormat());
        settings.setTheme(dto.getTheme());
        return settings;
    }

    public void updateUserSettingsFromDto(UserSettings settings, UserSettingsUpdateDTO dto) {
        if (dto.getLanguage() != null) {
            settings.setLanguage(dto.getLanguage());
        }
        if (dto.getDefaultCurrency() != null) {
            settings.setDefaultCurrency(Currency.valueOf(dto.getDefaultCurrency()));
        }
        if (dto.getDateFormat() != null) {
            settings.setDateFormat(dto.getDateFormat());
        }
        if (dto.getTimeFormat() != null) {
            settings.setTimeFormat(dto.getTimeFormat());
        }
        if (dto.getTheme() != null) {
            settings.setTheme(dto.getTheme());
        }
    }

    public UserSettingsResponseDTO toResponse(UserSettings settings) {
        UserSettingsResponseDTO response = new UserSettingsResponseDTO();
        response.setId(settings.getId());
        response.setUserId(settings.getUser().getId());
        response.setLanguage(settings.getLanguage());
        response.setDefaultCurrency(settings.getDefaultCurrency().toString());
        response.setDateFormat(settings.getDateFormat());
        response.setTimeFormat(settings.getTimeFormat());
        response.setTheme(settings.getTheme());
        response.setCreatedAt(settings.getCreatedAt());
        response.setUpdatedAt(settings.getUpdatedAt());
        return response;
    }
}
