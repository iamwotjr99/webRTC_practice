package com.practice.webRTC.Room.domain.vo;

import com.practice.webRTC.global.exception.CustomException;
import com.practice.webRTC.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class RoomCapacity {
    private final int value;

    public RoomCapacity(int value) {
        if (value < 2) {
            throw new CustomException(ErrorCode.ROOM_CAPACITY_UNDER_MIN);
        }

        if (value > 6) {
            throw new CustomException(ErrorCode.ROOM_CAPACITY_OVER_MAX);
        }

        this.value = value;
    }
}
