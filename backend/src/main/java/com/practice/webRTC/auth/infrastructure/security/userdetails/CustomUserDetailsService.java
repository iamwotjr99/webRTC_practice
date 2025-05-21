package com.practice.webRTC.auth.infrastructure.security.userdetails;

import com.practice.webRTC.user.domain.User;
import com.practice.webRTC.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetails loadUserByUserId(Long userId) {
        User user = userRepository.findById(userId);

        return new CustomUserDetails(user.getId(), user.getNicknameValue());
    }

}
