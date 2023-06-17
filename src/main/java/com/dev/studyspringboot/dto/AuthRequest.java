package com.dev.studyspringboot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @NotEmpty(message = "Username is EMPTY!")
    private String username;
    @NotEmpty(message = "Password is EMPTY!")
    private String password;
}
