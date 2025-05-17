package com.backenddiploma.dto.user;

import com.backenddiploma.models.enums.UserRole;
import lombok.Data;

@Data
public class UserCreateDTO {

    private String username;
    private String email;
    private String passwordHash;
    private UserRole role;
    private String profilePictureUrl;
}
