package com.practice.webRTC.user.domain;

import static org.assertj.core.api.Assertions.*;

import com.practice.webRTC.global.exception.CustomException;
import com.practice.webRTC.global.exception.ErrorCode;
import com.practice.webRTC.user.domain.vo.Password;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@DisplayName("User 도메인 테스트")
class UserTest {

    String nickname = "재석군";
    String rawPassword = "asd123";

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String encodedPassword = passwordEncoder.encode(rawPassword);

    @Test
    @DisplayName("createUser()로 유저 생성")
    void givenUser_whenCreateUser_thenReturnUser() {
        User user = User.createUser(nickname, encodedPassword);
        assertThat(user.getNicknameValue()).isEqualTo("재석군");
        assertThat(user.getPassword()).isNotEqualTo("asd1234");
    }

    @Test
    @DisplayName("createUser()를 할 때, 닉네임이 NULL값인 경우 예외 발생")
    void givenNullNickname_whenCreateUser_thenThrowsNullNicknameError() {
        assertThatThrownBy(() -> User.createUser("", encodedPassword))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.NICKNAME_IS_NULL.getMessage());
    }

    @Test
    @DisplayName("createUser()를 할 때, 닉네임이 2글자 미만인 경우 예외 발생")
    void givenTooShortNickname_whenCreateUser_thenThrowsTooShortError() {
        assertThatThrownBy(() -> User.createUser("1", encodedPassword))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.NICKNAME_TOO_SHORT.getMessage());
    }

    @Test
    @DisplayName("createUser()를 할 때, 닉네임이 8글자 초과인 경우 예외 발생")
    void givenTooLongNickname_whenCreateUser_thenThrowsTooLongError() {
        assertThatThrownBy(() -> User.createUser("123456789", encodedPassword))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.NICKNAME_TOO_LONG.getMessage());
    }

    @Test
    @DisplayName("validationRawPassword()를 할 때, 비밀번호가 NULL값 인 경우 예외 발생")
    void givenNullPassword_whenValidateRawPassword_thenThrowsNullPasswordError() {
        assertThatThrownBy(() -> Password.validateRawPassword(""))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.PASSWORD_IS_NULL.getMessage());
    }

    @Test
    @DisplayName("validationRawPassword()를 할 때, 비밀번호가 4글자 미만 인 경우 예외 발생")
    void givenTooShortPassword_whenValidateRawPassword_thenThrowsTooShortPasswordError() {
        assertThatThrownBy(() -> Password.validateRawPassword("123"))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.PASSWORD_TOO_SHORT.getMessage());
    }

    @Test
    @DisplayName("validationRawPassword()를 할 때, 비밀번호가 15글자 초과 인 경우 예외 발생")
    void givenTooLongPassword_whenValidateRawPassword_thenThrowsTooLongPasswordError() {
        assertThatThrownBy(() -> Password.validateRawPassword("1234567312321312321414"))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.PASSWORD_TOO_LONG.getMessage());
    }

}
