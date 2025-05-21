package com.practice.webRTC.room.domain;

import com.practice.webRTC.room.domain.vo.RoomCapacity;
import com.practice.webRTC.room.domain.vo.RoomTitle;
import com.practice.webRTC.global.exception.CustomException;
import com.practice.webRTC.global.exception.ErrorCode;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Room {
    private final Long id;
    private final Long userId;
    private final RoomTitle title;
    private int participant; // 해당 속성은 Redis로 실시간 관리
    private final RoomCapacity capacity;
    private final LocalDateTime createdAt;

    private Room(Long id, Long userId, RoomTitle title, int participant, RoomCapacity capacity, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.participant = participant;
        this.capacity = capacity;
        this.createdAt = createdAt;
    }

    public static Room createRoom(Long userId, String title, int capacity) {
        RoomTitle roomTitle = new RoomTitle(title);
        RoomCapacity roomCapacity = new RoomCapacity(capacity);
        return new Room(null, userId, roomTitle, 0, roomCapacity, LocalDateTime.now());
    }

    public void addParticipant() {
        if (isFull()) {
            throw new CustomException(ErrorCode.ROOM_IS_FULL);
        }

        this.participant++;
    }

    public void removeParticipant() {
        if (this.participant > 0) {
            this.participant--;
        }
    }

    public boolean isFull() {
        return this.participant >= getCapacityValue();
    }

    public boolean canJoin() {
        return !isFull();
    }

    public String getTitleValue() {
        return this.title.value();
    }

    public int getCapacityValue() {
        return this.capacity.value();
    }
}
