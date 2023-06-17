package com.dev.studyspringboot.dto;

import com.dev.studyspringboot.model.Order;
import com.dev.studyspringboot.model.UserRole;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    @NotEmpty(message = "Username is required!")
    private String username;
    @NotEmpty(message = "Password is required!")
    private String password;
    @NotEmpty(message = "Email is required!")
    private String email;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private List<UserRole> userRoles = new ArrayList<>();
    private List<Order> orders;
}
