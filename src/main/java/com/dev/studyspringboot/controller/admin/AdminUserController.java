package com.dev.studyspringboot.controller.admin;

import com.dev.studyspringboot.dto.ApiResponse;
import com.dev.studyspringboot.dto.UserDTO;
import com.dev.studyspringboot.model.User;
import com.dev.studyspringboot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
public class AdminUserController {
    @Autowired
    private IUserService iUserService;

    @PostMapping("/create")
    public ResponseEntity<?> addUser(
            @Validated @RequestBody UserDTO user )
    {
        iUserService.addUser(user);
        ApiResponse response = ApiResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("User created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable("id") Long userId,
            @Validated @RequestBody UserDTO user )
    {
        iUserService.updateUser(userId, user);
        ApiResponse response = ApiResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("User updated successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable("id") Long userId )
    {
        iUserService.deleteUser(userId);
        ApiResponse response = ApiResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("User deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllUser()
    {
        List<User> users = iUserService.getAllUser();
        ApiResponse response = ApiResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get list user successfully")
                .data(users)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<?> getOneUser(
            @PathVariable("id") Long userId )
    {
        User user = iUserService.getOneUser(userId);
        ApiResponse response = ApiResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get user successfully")
                .data(user)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
