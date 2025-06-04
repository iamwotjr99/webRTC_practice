package com.practice.webRTC.user.domain.repository;

import com.practice.webRTC.user.domain.User;
import java.util.List;

public interface UserRepository {
    User save(User user);
    User findById(Long userId);
    User findByNickname(String nickname);
    boolean existsByNickname(String nickname);
    List<User> findAllById(List<Long> userIds);
}
