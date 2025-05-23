package com.practice.webRTC.user.application;

import com.practice.webRTC.user.application.dto.GetUserInfoResDto;
import com.practice.webRTC.user.domain.User;
import com.practice.webRTC.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public GetUserInfoResDto getUserInfoById(Long userId) {
        User user = userRepository.findById(userId);

        return new GetUserInfoResDto(user.getId(), user.getNicknameValue());
    }

}
