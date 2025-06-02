package com.backenddiploma.unit.services;

import com.backenddiploma.config.exceptions.AlreadyExistsException;
import com.backenddiploma.config.exceptions.NotFoundException;
import com.backenddiploma.dto.usersettings.UserSettingsCreateDTO;
import com.backenddiploma.dto.usersettings.UserSettingsResponseDTO;
import com.backenddiploma.dto.usersettings.UserSettingsUpdateDTO;
import com.backenddiploma.mappers.UserSettingsMapper;
import com.backenddiploma.models.User;
import com.backenddiploma.models.UserSettings;
import com.backenddiploma.repositories.UserRepository;
import com.backenddiploma.repositories.UserSettingsRepository;
import com.backenddiploma.services.UserSettingsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSettingsServiceTest {

    @InjectMocks
    private UserSettingsService userSettingsService;

    @Mock
    private UserSettingsRepository userSettingsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSettingsMapper userSettingsMapper;

    // === create ===

    @Test
    void whenCreateSettings_thenSaveAndReturn() {
        UserSettingsCreateDTO dto = new UserSettingsCreateDTO();
        dto.setUserId(1L);

        User user = new User();
        user.setId(1L);

        UserSettings settings = new UserSettings();

        when(userSettingsRepository.existsByUserId(1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userSettingsMapper.toEntity(dto, user)).thenReturn(settings);
        when(userSettingsRepository.save(settings)).thenReturn(settings);
        when(userSettingsMapper.toResponse(settings)).thenReturn(new UserSettingsResponseDTO());

        UserSettingsResponseDTO result = userSettingsService.create(dto);

        assertNotNull(result);
        verify(userSettingsRepository).save(settings);
    }

    @Test
    void whenCreateSettings_alreadyExists_thenThrow() {
        UserSettingsCreateDTO dto = new UserSettingsCreateDTO();
        dto.setUserId(1L);

        when(userSettingsRepository.existsByUserId(1L)).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> userSettingsService.create(dto));
    }

    @Test
    void whenCreateSettings_userNotFound_thenThrow() {
        UserSettingsCreateDTO dto = new UserSettingsCreateDTO();
        dto.setUserId(1L);

        when(userSettingsRepository.existsByUserId(1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userSettingsService.create(dto));
    }

    // === getByUserId ===

    @Test
    void whenGetByUserId_thenReturnSettings() {
        UserSettings settings = new UserSettings();

        when(userSettingsRepository.findByUserId(1L)).thenReturn(Optional.of(settings));
        when(userSettingsMapper.toResponse(settings)).thenReturn(new UserSettingsResponseDTO());

        UserSettingsResponseDTO result = userSettingsService.getByUserId(1L);

        assertNotNull(result);
    }

    @Test
    void whenGetByUserId_notFound_thenThrow() {
        when(userSettingsRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userSettingsService.getByUserId(1L));
    }

    // === update ===

    @Test
    void whenUpdateSettings_thenSaveAndReturn() {
        UserSettingsUpdateDTO dto = new UserSettingsUpdateDTO();

        UserSettings settings = new UserSettings();

        when(userSettingsRepository.findById(1L)).thenReturn(Optional.of(settings));
        doNothing().when(userSettingsMapper).updateUserSettingsFromDto(settings, dto);
        when(userSettingsRepository.save(settings)).thenReturn(settings);
        when(userSettingsMapper.toResponse(settings)).thenReturn(new UserSettingsResponseDTO());

        UserSettingsResponseDTO result = userSettingsService.update(1L, dto);

        assertNotNull(result);
        verify(userSettingsRepository).save(settings);
    }

    @Test
    void whenUpdateSettings_notFound_thenThrow() {
        UserSettingsUpdateDTO dto = new UserSettingsUpdateDTO();

        when(userSettingsRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userSettingsService.update(1L, dto));
    }

    // === delete ===

    @Test
    void whenDeleteSettings_thenDelete() {
        UserSettings settings = new UserSettings();

        when(userSettingsRepository.findById(1L)).thenReturn(Optional.of(settings));

        userSettingsService.delete(1L);

        verify(userSettingsRepository).delete(settings);
    }

    @Test
    void whenDeleteSettings_notFound_thenThrow() {
        when(userSettingsRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userSettingsService.delete(1L));
    }
}
