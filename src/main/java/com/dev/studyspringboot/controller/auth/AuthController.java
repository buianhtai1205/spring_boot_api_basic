package com.dev.studyspringboot.controller.auth;

import com.dev.studyspringboot.config.services.CustomAuthenticationManager;
import com.dev.studyspringboot.dto.AuthRequest;
import com.dev.studyspringboot.config.jwt.JwtService;
import com.dev.studyspringboot.dto.JwtResponse;
import com.dev.studyspringboot.dto.RefreshTokenRequest;
import com.dev.studyspringboot.exception.ResourceNotFoundException;
import com.dev.studyspringboot.model.RefreshToken;
import com.dev.studyspringboot.service.IRefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private CustomAuthenticationManager authenticationManager;
    @Autowired
    private IRefreshTokenService refreshTokenService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateAndGetJwt(@Validated @RequestBody AuthRequest authRequest )
    {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()
        ));
        if (authentication.isAuthenticated()) {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());
            JwtResponse jwtResponse = JwtResponse.builder()
                    .accessToken(jwtService.generateToken(authRequest.getUsername()))
                    .token(refreshToken.getToken())
                    .build();
            return ResponseEntity.ok(jwtResponse);
        } else throw new RuntimeException("Internal Server Error");
    }

    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(user.getUsername());
                    return JwtResponse.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequest.getToken())
                            .build();
                }).orElseThrow(() -> new ResourceNotFoundException("Refresh token is not in database"));
    }

    @GetMapping("/login")
    public String loginWithAzure() {
        return "Login successful!";
    }
}
