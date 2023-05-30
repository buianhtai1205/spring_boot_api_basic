package com.dev.studyspringboot.service;

import com.dev.studyspringboot.model.User;

import java.util.List;

public interface IUserService {
    void addUser(User user);
    void updateUser(Long userId, User user);
    void deleteUser(Long userId);
    List<User> getAllUser();
    User getOneUser(Long userId);
}
