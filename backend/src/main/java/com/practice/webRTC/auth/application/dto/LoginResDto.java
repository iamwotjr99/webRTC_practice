package com.practice.webRTC.auth.application.dto;

import com.practice.webRTC.user.domain.User;
import lombok.Builder;

@Builder
public record LoginResDto(Long userId, String nickname, String accessToken) {

    public static LoginResDto from(User user, String accessToken) {
        return LoginResDto.builder()
                .userId(user.getId())
                .nickname(user.getNicknameValue())
                .accessToken(accessToken)
                .build();
    }
}
