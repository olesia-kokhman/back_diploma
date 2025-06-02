package com.backenddiploma.dto.security;

import com.backenddiploma.models.enums.UserRole;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    private String username;
    private String email;
    private String password;
    private UserRole role;
}
