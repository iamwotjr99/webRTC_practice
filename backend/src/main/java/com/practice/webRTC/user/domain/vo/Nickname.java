package com.practice.webRTC.user.domain.vo;

import com.practice.webRTC.global.exception.CustomException;
import com.practice.webRTC.global.exception.ErrorCode;

public record Nickname(String value) {

    public Nickname {
        // 닉네임은 2글자 이상 8글자 이하
        if (value == null || value.isEmpty()) {
            throw new CustomException(ErrorCode.NICKNAME_IS_NULL);
        }

        if (value.length() < 2) {
            throw new CustomException(ErrorCode.NICKNAME_TOO_SHORT);
        }

        if (value.length() > 8) {
            throw new CustomException(ErrorCode.NICKNAME_TOO_LONG);
        }
    }
}
