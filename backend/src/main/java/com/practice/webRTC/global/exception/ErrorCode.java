package com.practice.webRTC.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 방 관련
    ROOM_IS_NOT_FOUND(404, "해당 스터디 방을 찾을 수 없습니다."),
    ROOM_TITLE_ESSENTIAL(400, "스터디 방 제목은 필수입니다."),
    ROOM_TITLE_TOO_SHORT(400, "스터디 방 제목은 2자 이상이어야 합니다."),
    ROOM_TITLE_TOO_LONG(400, "스터디 방 제목은 20자 이하여야 합니다."),
    ROOM_CAPACITY_ESSENTIAL(400, "스터디 방 인원수는 필수입니다."),
    ROOM_CAPACITY_OVER_MAX(400, "스터디 방 인원수는 6명 이하여야 합니다."),
    ROOM_CAPACITY_UNDER_MIN(400, "스터디 방 인원수는 2명 이상이어야 합니다."),
    ROOM_IS_FULL(400, "현재 스터디 방 정원이 가득 찼습니다.");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
