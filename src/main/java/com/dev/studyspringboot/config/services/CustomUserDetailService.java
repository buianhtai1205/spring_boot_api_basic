package com.dev.studyspringboot.config.services;

import com.dev.studyspringboot.exception.ResourceNotFoundException;
import com.dev.studyspringboot.model.User;
import com.dev.studyspringboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserAndRoleToLogin(username);
        return user.map(CustomUserDetails::new)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
}
