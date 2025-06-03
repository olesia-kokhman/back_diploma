package com.backenddiploma.unit.services;
import com.backenddiploma.config.exceptions.AlreadyExistsException;
import com.backenddiploma.config.exceptions.NotFoundException;
import com.backenddiploma.dto.user.UserCreateDTO;
import com.backenddiploma.dto.user.UserResponseDTO;
import com.backenddiploma.dto.user.UserUpdateDTO;
import com.backenddiploma.mappers.UserMapper;
import com.backenddiploma.models.Category;
import com.backenddiploma.models.User;
import com.backenddiploma.repositories.CategoryRepository;
import com.backenddiploma.repositories.UserRepository;
import com.backenddiploma.services.DefaultCategoryLoader;
import com.backenddiploma.services.UserService;
import com.backenddiploma.services.integrations.CloudinaryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private DefaultCategoryLoader defaultCategoryLoader;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    // === create ===

    @Test
    void whenCreateUser_thenSaveAndReturn() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("test@example.com");

        User user = new User();

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userMapper.toEntity(dto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(defaultCategoryLoader.loadDefaultCategoriesForUser(user)).thenReturn(List.of());
        when(userMapper.toResponse(user)).thenReturn(new UserResponseDTO());

        UserResponseDTO result = userService.create(dto);

        assertNotNull(result);
        verify(userRepository).save(user);
        verify(categoryRepository).saveAll(List.of());
    }

    @Test
    void whenCreateUser_emailExists_thenThrow() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("test@example.com");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> userService.create(dto));
    }

    // === getById ===

    @Test
    void whenGetById_thenReturnUser() {
        User user = new User();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(new UserResponseDTO());

        UserResponseDTO result = userService.getById(1L);

        assertNotNull(result);
    }

    @Test
    void whenGetById_notFound_thenThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getById(1L));
    }

    // === update ===

    @Test
    void whenUpdateUser_thenSaveAndReturn() {
        User user = new User();
        user.setEmail("old@example.com");

        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setEmail("new@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(new UserResponseDTO());

        UserResponseDTO result = userService.update(1L, dto);

        assertNotNull(result);
        verify(userRepository).save(user);
        verify(userMapper).updateUserFromDto(user, dto);
    }

    @Test
    void whenUpdateUser_emailExists_thenThrow() {
        User user = new User();
        user.setEmail("old@example.com");

        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setEmail("existing@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> userService.update(1L, dto));
    }

    @Test
    void whenUpdateUser_notFound_thenThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.update(1L, new UserUpdateDTO()));
    }

    // === deleteAvatar ===

    @Test
    void whenDeleteAvatar_thenClearAndSave() {
        User user = new User();
        user.setProfilePicturePublicId("public_id");
        user.setProfilePictureUrl("url");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteAvatar(1L);

        verify(cloudinaryService).deleteFile("public_id");
        verify(userRepository).save(user);
        assertNull(user.getProfilePicturePublicId());
        assertNull(user.getProfilePictureUrl());
    }

        @Test
    void whenDeleteAvatar_notFound_thenThrow() {
        User user = new User();
        user.setProfilePicturePublicId(null); // це імітує умову

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteAvatar(1L);

        verify(userRepository, never()).save(any()); // перевірка, що save не викликано
        verifyNoInteractions(cloudinaryService); // опціонально: cloudinary теж не чіпаємо
    }

    // === delete ===

    @Test
    void whenDeleteUser_thenDelete() {
        User user = new User();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.delete(1L);

        verify(userRepository).delete(user);
    }

    @Test
    void whenDeleteUser_notFound_thenThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.delete(1L));
    }

    // === getAll ===

    @Test
    void whenGetAllUsers_thenReturnList() {
        User user1 = new User();
        User user2 = new User();

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(userMapper.toResponse(user1)).thenReturn(new UserResponseDTO());
        when(userMapper.toResponse(user2)).thenReturn(new UserResponseDTO());

        List<UserResponseDTO> result = userService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

}
