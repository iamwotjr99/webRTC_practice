package com.practice.webRTC.room.ui;

import com.practice.webRTC.room.domain.Room;
import lombok.Builder;

@Builder
public record RoomListResDto(
        Long roomId,
        String title,
        int capacity,
        int participant
) {
    public static RoomListResDto from(Room room) {
        return RoomListResDto.builder()
                .roomId(room.getId())
                .title(room.getTitleValue())
                .capacity(room.getCapacityValue())
                .participant(room.getParticipant())
                .build();
    }
}
