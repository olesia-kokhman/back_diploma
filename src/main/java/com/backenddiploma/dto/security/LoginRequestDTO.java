package com.backenddiploma.dto.security;

import lombok.Data;

@Data
public class LoginRequestDTO {

    private String email;
    private String password;
}
