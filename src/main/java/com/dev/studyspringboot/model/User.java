package com.dev.studyspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    @Nullable
    private String fullName;
    @Nullable
    private String address;
    @Nullable
    private String phoneNumber;
    @Nullable
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Nullable
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "user")
    private List<UserRole> userRoles = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
