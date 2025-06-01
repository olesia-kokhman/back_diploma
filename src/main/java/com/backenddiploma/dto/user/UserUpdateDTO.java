package com.backenddiploma.dto.user;

import com.backenddiploma.models.enums.UserRole;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserUpdateDTO {

    private String username;
    private String email;
    private String passwordHash;
    private UserRole role;
    private String webHookUrl;
    private String monobankToken;
    private MultipartFile file;
}
