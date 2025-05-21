package com.practice.webRTC.auth.repository;

import java.util.Optional;

public interface RefreshTokenRepository {
    void save(Long userId, String refreshToken, long expirationMs);
    Optional<String> findByUserId(Long userId);
    void delete(Long userId);
}
