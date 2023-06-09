package com.dev.studyspringboot.controller.auth;

import com.dev.studyspringboot.config.services.CustomAuthenticationManager;
import com.dev.studyspringboot.dto.AuthRequest;
import com.dev.studyspringboot.config.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private CustomAuthenticationManager authenticationManager;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateAndGetJwt(@RequestBody AuthRequest authRequest )
    {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()
        ));
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok(jwtService.generateToken(authRequest.getUsername()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
