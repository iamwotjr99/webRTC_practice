package com.practice.webRTC.room.domain.vo;

import com.practice.webRTC.global.exception.CustomException;
import com.practice.webRTC.global.exception.ErrorCode;

public record RoomCapacity(int value) {

    public RoomCapacity {
        if (value < 2) {
            throw new CustomException(ErrorCode.ROOM_CAPACITY_UNDER_MIN);
        }

        if (value > 6) {
            throw new CustomException(ErrorCode.ROOM_CAPACITY_OVER_MAX);
        }

    }
}
