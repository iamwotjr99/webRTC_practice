package com.practice.webRTC.auth.repository;

import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository{

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(Long userId, String refreshToken, long expirationMs) {
        redisTemplate.opsForValue().set("refresh:" + userId, refreshToken, Duration.ofMillis(expirationMs));
    }

    @Override
    public Optional<String> findByUserId(Long userId) {
        String token = redisTemplate.opsForValue().get("refresh:" + userId);
        return Optional.ofNullable(token);
    }

    @Override
    public void delete(Long userId) {
        redisTemplate.delete("refresh:" + userId);
    }
}
