package com.practice.webRTC.room.application.dto;

import com.practice.webRTC.room.domain.Room;
import lombok.Builder;

@Builder
public record JoinRoomResDto(Long roomId, String roomTitle) {
    public static JoinRoomResDto from(Room room) {
        return JoinRoomResDto.builder()
                .roomId(room.getId())
                .roomTitle(room.getTitleValue())
                .build();
    }
}
