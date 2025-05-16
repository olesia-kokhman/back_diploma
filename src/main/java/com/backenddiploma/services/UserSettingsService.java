package com.backenddiploma.services;

import com.backenddiploma.models.Currency;
import com.backenddiploma.models.LanguageAbbreviation;
import com.backenddiploma.models.Theme;
import com.backenddiploma.models.UserSettings;
import com.backenddiploma.repositories.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSettingsService {

    private final UserSettingsRepository userRepository;

    public UserSettings getSettings(Long userId) {
        return userRepository.findByUserId(userId);
    }

    @Transactional
    public UserSettings addSettingsIfAbsent(Long userId) {
        if(getSettings(userId) == null) {
            UserSettings settings = new UserSettings();
            settings.setUserId(userId);
            settings.setLanguage(LanguageAbbreviation.EN);
            settings.setDefaultCurrency(Currency.UAH);
            settings.setDateFormat("yyyy-MM-dd");
            settings.setTimeFormat("HH:mm:ss");
            settings.setTheme(Theme.LIGHT);
            return userRepository.save(settings);
        } else {
            throw new RuntimeException("User settings for the user alrealy exists");
        }
    }

    @Transactional
    public UserSettings updateUserSettings(Long userId, UserSettings userSettings) {
        if(getSettings(userId) != null) {
            userSettings.setId(userId);
            return userRepository.save(userSettings);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
