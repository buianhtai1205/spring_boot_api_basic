package com.dev.studyspringboot.service;

import com.dev.studyspringboot.model.RefreshToken;

import java.util.Optional;

public interface IRefreshTokenService {
    RefreshToken createRefreshToken(String username);

    Optional<RefreshToken> findByToken(String token);

    RefreshToken verifyExpiration(RefreshToken token);
}
