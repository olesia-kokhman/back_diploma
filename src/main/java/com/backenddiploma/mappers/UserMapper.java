package com.backenddiploma.mappers;

import com.backenddiploma.dto.user.UserCreateDTO;
import com.backenddiploma.dto.user.UserResponseDTO;
import com.backenddiploma.dto.user.UserUpdateDTO;
import com.backenddiploma.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserCreateDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(dto.getPasswordHash());
        user.setRole(dto.getRole());
        user.setProfilePictureUrl(dto.getProfilePictureUrl());
        return user;
    }

    public void updateUserFromDto(User user, UserUpdateDTO dto) {
        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPasswordHash() != null) {
            user.setPasswordHash(dto.getPasswordHash());
        }
        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }

    }

    public UserResponseDTO toResponse(User user) {
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setProfilePictureUrl(user.getProfilePictureUrl());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
