package com.dev.studyspringboot.service;

import com.dev.studyspringboot.exception.WarningException;
import com.dev.studyspringboot.model.RefreshToken;
import com.dev.studyspringboot.repository.RefreshTokenRepository;
import com.dev.studyspringboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements IRefreshTokenService{
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(86400*10)) // 10 days
                .user(userRepository.findByUsernameAndDeletedAtIsNull(username))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new WarningException(token.getToken() + " . RefreshToken was expired. Please make new sign in!");
        }
        return token;
    }
}
