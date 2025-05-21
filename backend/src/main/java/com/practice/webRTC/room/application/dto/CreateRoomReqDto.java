package com.practice.webRTC.room.application.dto;

import lombok.Builder;

@Builder
public record CreateRoomReqDto (
        // userId : 추후 회원 기능 도입시 token에서 추출 할 예정
        Long userId,
        String title,
        int capacity
) {
    public static CreateRoomReqDto from(Long userId, String title, int capacity) {
        return CreateRoomReqDto.builder()
                .userId(userId)
                .title(title)
                .capacity(capacity)
                .build();
    }
}
