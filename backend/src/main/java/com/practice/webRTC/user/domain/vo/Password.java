package com.practice.webRTC.user.domain.vo;

import com.practice.webRTC.global.exception.CustomException;
import com.practice.webRTC.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

public record Password(String encodedValue) {
    // Spring Security 도입 시, 암호화 할 예정

    public static Password fromEncodedValue(String encodedValue) {
        return new Password(encodedValue);
    }

    // password는 4글자 이상 15글자 이하
    public static void validateRawPassword(String value) {
        if (value == null || value.isEmpty()) {
            throw new CustomException(ErrorCode.PASSWORD_IS_NULL);
        }

        if (value.length() < 4) {
            throw new CustomException(ErrorCode.PASSWORD_TOO_SHORT);
        }

        if (value.length() > 15) {
            throw new CustomException(ErrorCode.PASSWORD_TOO_LONG);
        }
    }

    public boolean matches(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, this.encodedValue);
    }
}
