package com.backenddiploma.unit.mappers;

import com.backenddiploma.dto.user.UserCreateDTO;
import com.backenddiploma.dto.user.UserResponseDTO;
import com.backenddiploma.dto.user.UserUpdateDTO;
import com.backenddiploma.mappers.UserMapper;
import com.backenddiploma.models.User;
import com.backenddiploma.models.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void testToEntity() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername("testuser");
        dto.setEmail("test@example.com");
        dto.setPasswordHash("hashedpassword");
        dto.setRole(UserRole.ADMIN);

        User user = userMapper.toEntity(dto);

        assertThat(user.getUsername()).isEqualTo("testuser");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPasswordHash()).isEqualTo("hashedpassword");
        assertThat(user.getRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    void testUpdateUserFromDto() {
        User user = new User();
        user.setUsername("olduser");
        user.setEmail("old@example.com");
        user.setPasswordHash("oldpassword");
        user.setRole(UserRole.ADMIN);

        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUsername("newuser");
        dto.setEmail("new@example.com");
        dto.setPasswordHash("newpassword");
        dto.setRole(UserRole.ADMIN);

        userMapper.updateUserFromDto(user, dto);

        assertThat(user.getUsername()).isEqualTo("newuser");
        assertThat(user.getEmail()).isEqualTo("new@example.com");
        assertThat(user.getPasswordHash()).isEqualTo("newpassword");
        assertThat(user.getRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    void testToResponse() {
        User user = new User();
        user.setId(42L);
        user.setUsername("myuser");
        user.setEmail("myuser@example.com");
        user.setRole(UserRole.ADMIN);
        user.setProfilePictureUrl("http://image.url/pic.png");

        user.setCreatedAt(LocalDateTime.of(2025, 6, 1, 12, 0));
        user.setUpdatedAt(LocalDateTime.of(2025, 6, 2, 15, 30));

        UserResponseDTO response = userMapper.toResponse(user);

        assertThat(response.getId()).isEqualTo(42L);
        assertThat(response.getUsername()).isEqualTo("myuser");
        assertThat(response.getEmail()).isEqualTo("myuser@example.com");
        assertThat(response.getRole()).isEqualTo(UserRole.ADMIN);
        assertThat(response.getProfilePictureUrl()).isEqualTo("http://image.url/pic.png");
        assertThat(response.getCreatedAt()).isEqualTo(LocalDateTime.of(2025, 6, 1, 12, 0));
        assertThat(response.getUpdatedAt()).isEqualTo(LocalDateTime.of(2025, 6, 2, 15, 30));
    }
}
