package com.practice.webRTC.Room.domain.vo;

import com.practice.webRTC.global.exception.CustomException;
import com.practice.webRTC.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class RoomTitle {
    private String value;

    public RoomTitle(String value) {
        if (value == null || value.isEmpty()) {
            throw new CustomException(ErrorCode.ROOM_TITLE_ESSENTIAL);
        }

        if (value.length() > 20) {
            throw new CustomException(ErrorCode.ROOM_TITLE_TOO_LONG);
        }

        if (value.length() < 2) {
            throw new CustomException(ErrorCode.ROOM_TITLE_TOO_SHORT);
        }

        this.value = value;
    }
}
