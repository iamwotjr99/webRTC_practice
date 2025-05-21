package com.practice.webRTC.auth.application;

import com.practice.webRTC.auth.application.dto.LoginReqDto;
import com.practice.webRTC.auth.application.dto.LoginResDto;
import com.practice.webRTC.auth.application.dto.RegisterReqDto;
import com.practice.webRTC.auth.infrastructure.cookie.CookieUtil;
import com.practice.webRTC.auth.infrastructure.jwt.JwtProvider;
import com.practice.webRTC.auth.repository.RefreshTokenRepository;
import com.practice.webRTC.global.exception.CustomException;
import com.practice.webRTC.global.exception.ErrorCode;
import com.practice.webRTC.user.domain.User;
import com.practice.webRTC.user.domain.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder encoder;

    public boolean isDuplicatedNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.NICKNAME_IS_DUPLICATED);
        }

        return true;
    }

    public LoginResDto login(LoginReqDto dto, HttpServletResponse response) {
        User user = userRepository.findByNickname(dto.nickname());

        if (!user.getPassword().matches(dto.password(), encoder)) {
            throw new CustomException(ErrorCode.PASSWORD_IS_INVALID);
        }

        String accessToken = jwtProvider.createAccessToken(user.getId());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        refreshTokenRepository.save(user.getId(), refreshToken, jwtProvider.getRefreshTokenExpiration());
        CookieUtil.setRefreshTokenInCookie(response, refreshToken, jwtProvider.getRefreshTokenExpiration());

        return LoginResDto.from(user, accessToken);
    }

    public String register(RegisterReqDto dto) {
        String encodePassword = encoder.encode(dto.password());
        User user = User.createUser(dto.nickname(), encodePassword);
        userRepository.save(user);

        return "회원가입 성공";
    }

    public String refreshAccessToken(String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.TOKEN_INVALID);
        }

        Long userId = jwtProvider.getUserIdFromToken(refreshToken);
        String savedToken = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.TOKEN_IS_NOT_FOUND));

        if (!savedToken.equals(refreshToken)) {
            throw new CustomException(ErrorCode.TOKEN_INVALID);
        }

        String accessToken = jwtProvider.createAccessToken(userId);
        return accessToken;
    }

}
