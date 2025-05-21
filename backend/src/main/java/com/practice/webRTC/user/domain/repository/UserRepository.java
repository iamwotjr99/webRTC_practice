package com.practice.webRTC.user.domain.repository;

import com.practice.webRTC.user.domain.User;

public interface UserRepository {
    User save(User user);
    User findById(Long userId);
    User findByNickname(String nickname);
    boolean existsByNickname(String nickname);
}
