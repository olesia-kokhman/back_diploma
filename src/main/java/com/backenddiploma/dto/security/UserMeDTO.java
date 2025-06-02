package com.backenddiploma.dto.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserMeDTO {
    private Long id;
    private String username;
    private String email;
}
