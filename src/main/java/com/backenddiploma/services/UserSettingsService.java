package com.backenddiploma.services;

import com.backenddiploma.dto.usersettings.UserSettingsCreateDTO;
import com.backenddiploma.dto.usersettings.UserSettingsResponseDTO;
import com.backenddiploma.dto.usersettings.UserSettingsUpdateDTO;
import com.backenddiploma.config.exceptions.AlreadyExistsException;
import com.backenddiploma.config.exceptions.NotFoundException;
import com.backenddiploma.mappers.UserSettingsMapper;
import com.backenddiploma.models.User;
import com.backenddiploma.models.UserSettings;
import com.backenddiploma.repositories.UserRepository;
import com.backenddiploma.repositories.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSettingsService {

    private final UserSettingsRepository userSettingsRepository;
    private final UserRepository userRepository;
    private final UserSettingsMapper userSettingsMapper;

    @Transactional
    public UserSettingsResponseDTO create(UserSettingsCreateDTO dto) {
        if (userSettingsRepository.existsByUserId(dto.getUserId())) {
            throw new AlreadyExistsException("Settings already exist for user: " + dto.getUserId());
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + dto.getUserId()));

        UserSettings settings = userSettingsMapper.toEntity(dto, user);
        UserSettings savedSettings = userSettingsRepository.save(settings);

        return userSettingsMapper.toResponse(savedSettings);
    }

    @Transactional(readOnly = true)
    public UserSettingsResponseDTO getByUserId(Long userId) {
        UserSettings settings = userSettingsRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Settings not found for user id: " + userId));
        return userSettingsMapper.toResponse(settings);
    }

    @Transactional
    public UserSettingsResponseDTO update(Long id, UserSettingsUpdateDTO dto) {
        UserSettings settings = userSettingsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Settings not found with id: " + id));

        userSettingsMapper.updateUserSettingsFromDto(settings, dto);
        UserSettings updatedSettings = userSettingsRepository.save(settings);

        return userSettingsMapper.toResponse(updatedSettings);
    }

    @Transactional
    public void delete(Long id) {
        UserSettings settings = userSettingsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Settings not found with id: " + id));
        userSettingsRepository.delete(settings);
    }
}
