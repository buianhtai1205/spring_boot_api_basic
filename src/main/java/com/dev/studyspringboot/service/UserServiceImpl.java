package com.dev.studyspringboot.service;

import com.dev.studyspringboot.dto.UserDTO;
import com.dev.studyspringboot.exception.NullException;
import com.dev.studyspringboot.exception.ResourceNotFoundException;
import com.dev.studyspringboot.model.Role;
import com.dev.studyspringboot.model.User;
import com.dev.studyspringboot.model.UserRole;
import com.dev.studyspringboot.repository.RoleRepository;
import com.dev.studyspringboot.repository.UserRepository;
import com.dev.studyspringboot.repository.UserRoleRepository;
import com.dev.studyspringboot.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void addUser(UserDTO user) {
        if (user != null) {
            if (!userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail())) {
                PasswordEncoder encoder = new BCryptPasswordEncoder();
                user.setPassword(encoder.encode(user.getPassword()));
                User newUser = new User();
                ReflectionUtils.copyNonNullFields(user, newUser);

                newUser = userRepository.saveAndFlush(newUser);

                Role defaultRole = roleRepository.findByRoleName("USER");

                userRoleRepository.save( UserRole.builder()
                                .user(newUser)
                                .role(defaultRole)
                                .build() );

            } else throw new DuplicateKeyException("Username or Email already exists!");
        } else throw new NullException("User is null value!");
    }

    @Override
    public void updateUser(Long userId, UserDTO user) {
        if (user != null) {
            User existingUser = userRepository.findByIdAndDeletedAtIsNull(userId);
            if (existingUser != null) {
                user.setUserRoles(existingUser.getUserRoles());

                ReflectionUtils.copyNonNullFields(user, existingUser);

                if (existingUser.getId().equals(userId)) {
                    userRepository.save(existingUser);
                } else throw new RuntimeException("Has Error when edit request!");

            } else throw new ResourceNotFoundException("User has id: " + userId + " NOT exist!");
        } else throw new NullException("User is null value!");
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        User existingUser = userRepository.findByIdAndDeletedAtIsNull(userId);
        if (existingUser != null) {
            userRepository.softDeleteById(userId);
        } else throw new ResourceNotFoundException("User has id: " + userId + " NOT exist!");
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAllByDeletedAtIsNull();
    }

    @Override
    public User getOneUser(Long userId) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId);
        if (user != null) {
            return user;
        } else throw new ResourceNotFoundException("User has id: " + userId + " NOT exist!");
    }
}
