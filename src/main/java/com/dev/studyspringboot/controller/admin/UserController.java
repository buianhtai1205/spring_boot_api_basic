package com.dev.studyspringboot.controller.admin;

import com.dev.studyspringboot.model.User;
import com.dev.studyspringboot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/user")
public class UserController {
    @Autowired
    private IUserService iUserService;

    @PostMapping("/create")
    public ResponseEntity<String> addUser(
            @Validated @RequestBody User user )
    {
        iUserService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable("id") Long userId,
            @RequestBody User user )
    {
        iUserService.updateUser(userId, user);
        return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable("id") Long userId )
    {
        iUserService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUser()
    {
        List<User> users = iUserService.getAllUser();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<User> getOneUser(
            @PathVariable("id") Long userId )
    {
        User user = iUserService.getOneUser(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
