package com.practice.webRTC.user.domain;

import com.practice.webRTC.user.domain.vo.Nickname;
import com.practice.webRTC.user.domain.vo.Password;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {
    private Long id;
    private final Nickname nickname;
    private final Password password;
    private final LocalDateTime createdAt;

    private User(Long id, Nickname nickname, Password password, LocalDateTime createdAt) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.createdAt = createdAt;
    }

    public static User createUser(String nickname, String encodedPassword) {
        Nickname n = new Nickname(nickname);
        Password p = Password.fromEncodedValue(encodedPassword);
        return new User(null, n, p, LocalDateTime.now());
    }

    public String getNicknameValue() {
        return this.nickname.value();
    }

    public String getPasswordValue() {
        return this.password.encodedValue();
    }
}
