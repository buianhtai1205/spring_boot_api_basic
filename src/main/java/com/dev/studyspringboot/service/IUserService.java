package com.dev.studyspringboot.service;

import com.dev.studyspringboot.dto.UserDTO;
import com.dev.studyspringboot.model.User;

import java.util.List;

public interface IUserService {
    void addUser(UserDTO user);
    void updateUser(Long userId, UserDTO user);
    void deleteUser(Long userId);
    List<User> getAllUser();
    User getOneUser(Long userId);
}
