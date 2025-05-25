package com.practice.webRTC.room.application.dto;

import lombok.Builder;

@Builder
public record CreateRoomReqDto (
        String title,
        int capacity
) {
    public static CreateRoomReqDto from(String title, int capacity) {
        return CreateRoomReqDto.builder()
                .title(title)
                .capacity(capacity)
                .build();
    }
}
