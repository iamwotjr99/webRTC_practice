package com.practice.webRTC.room.application.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record EnterRoomResDto(
        Long userId,
        List<Long> participants
) {
    public static EnterRoomResDto from(Long userId, List<Long> participants) {
        return EnterRoomResDto.builder()
                .userId(userId)
                .participants(participants)
                .build();
    }
}
