package com.practice.webRTC.room.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class RoomUser {

    private Long id;
    private Long userId;
    private Long roomId;
    private LocalDateTime joinedAt;

    public static RoomUser join(Long userId, Long roomId) {
        return new RoomUser(null, userId, roomId, LocalDateTime.now());
    }

    public void rejoin() {
        updateJoinedAt();
    }

    public void updateJoinedAt() {
        this.joinedAt = LocalDateTime.now();
    }
}
