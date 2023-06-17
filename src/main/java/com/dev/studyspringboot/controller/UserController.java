package com.dev.studyspringboot.controller;

import com.dev.studyspringboot.dto.DefaultResponse;
import com.dev.studyspringboot.dto.UserDTO;
import com.dev.studyspringboot.model.User;
import com.dev.studyspringboot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private IUserService iUserService;

    @PreAuthorize("permitAll")
    @PostMapping("/create")
    public ResponseEntity<?> addUser(
            @Validated @RequestBody UserDTO user )
    {
        iUserService.addUser(user);
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("User created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable("id") Long userId,
            @Validated @RequestBody UserDTO user )
    {
        iUserService.updateUser(userId, user);
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("User updated successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable("id") Long userId )
    {
        iUserService.deleteUser(userId);
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("User deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/show/{id}")
    public ResponseEntity<?> getOneUser(
            @PathVariable("id") Long userId )
    {
        User user = iUserService.getOneUser(userId);
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get user successfully")
                .data(user)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
