package com.practice.webRTC.room.domain.vo;

import com.practice.webRTC.global.exception.CustomException;
import com.practice.webRTC.global.exception.ErrorCode;

public record RoomTitle(String value) {

    public RoomTitle {
        if (value == null || value.isEmpty()) {
            throw new CustomException(ErrorCode.ROOM_TITLE_ESSENTIAL);
        }

        if (value.length() > 20) {
            throw new CustomException(ErrorCode.ROOM_TITLE_TOO_LONG);
        }

        if (value.length() < 2) {
            throw new CustomException(ErrorCode.ROOM_TITLE_TOO_SHORT);
        }

    }
}
