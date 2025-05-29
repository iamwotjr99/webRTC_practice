package com.practice.webRTC.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 토큰 관련
    TOKEN_INVALID(403, "유효하지 않는 토큰입니다."),
    TOKEN_IS_NOT_FOUND(404, "존재하지 않는 토큰입니다."),

    // 유저 관련
    NICKNAME_IS_NULL(400, "닉네임은 필수 입니다."),
    NICKNAME_TOO_SHORT(400, "닉네임은 2글자 이상이어야 합니다."),
    NICKNAME_TOO_LONG(400, "닉네임은 8글자 이하이어야 합니다."),
    NICKNAME_IS_DUPLICATED(400, "이미 존재하는 닉네임 입니다."),
    PASSWORD_IS_NULL(400, "비밀번호는 필수 입니다."),
    PASSWORD_TOO_SHORT(400, "비밀번호는 4글자 이상이어야 합니다."),
    PASSWORD_TOO_LONG(400, "비밀번호는 15글자 이하이어야 합니다."),
    PASSWORD_IS_INVALID(403, "유효하지 않는 비밀번호 입니다."),
    USER_IS_NOT_FOUND(404, "존재하지 않는 유저입니다."),
    USER_UNAUTHORIZED(403, "인증되지 않은 유저입니다."),

    // 방 관련
    ROOM_IS_NOT_FOUND(404, "해당 스터디 방을 찾을 수 없습니다."),
    ROOM_TITLE_ESSENTIAL(400, "스터디 방 제목은 필수입니다."),
    ROOM_TITLE_TOO_SHORT(400, "스터디 방 제목은 2자 이상이어야 합니다."),
    ROOM_TITLE_TOO_LONG(400, "스터디 방 제목은 20자 이하여야 합니다."),
    ROOM_CAPACITY_ESSENTIAL(400, "스터디 방 인원수는 필수입니다."),
    ROOM_CAPACITY_OVER_MAX(400, "스터디 방 인원수는 6명 이하여야 합니다."),
    ROOM_CAPACITY_UNDER_MIN(400, "스터디 방 인원수는 2명 이상이어야 합니다."),
    ROOM_IS_FULL(400, "현재 스터디 방 정원이 가득 찼습니다."),

    // 유저-방 관련
    ROOM_USER_IS_NOT_FOUND(404, "해당 유저와 해당 방에 대한 정보를 찾을 수 없습니다."),

    // 시그널링 관련
    SIGNALING_UNKNOWN_TYPE(400, "잘못된 시그널링 타입입니다.");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
