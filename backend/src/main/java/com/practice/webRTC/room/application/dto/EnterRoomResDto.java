package com.practice.webRTC.room.application.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record EnterRoomResDto(
        Long userId,
        List<ParticipantInfo> participants
) {
    public static EnterRoomResDto from(Long userId, List<ParticipantInfo> participants) {
        return EnterRoomResDto.builder()
                .userId(userId)
                .participants(participants)
                .build();
    }
}
