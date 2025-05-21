package com.practice.webRTC.auth.ui;

import com.practice.webRTC.auth.application.AuthService;
import com.practice.webRTC.auth.application.dto.LoginReqDto;
import com.practice.webRTC.auth.application.dto.LoginResDto;
import com.practice.webRTC.auth.application.dto.RegisterReqDto;
import com.practice.webRTC.global.common.ApiResponse;
import com.practice.webRTC.global.common.ResponseFactory;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/check-nickname")
    public ResponseEntity<ApiResponse<Boolean>> checkNickname(
            @RequestParam String nickname) {
        boolean result = authService.isDuplicatedNickname(nickname);

        return ResponseFactory.ok("사용가능한 닉네임", result);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResDto>> login(@RequestBody LoginReqDto dto,
            HttpServletResponse response) {
        LoginResDto result = authService.login(dto, response);

        return ResponseFactory.ok("로그인 성공", result);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody RegisterReqDto dto) {
        String result = authService.register(dto);

        return ResponseFactory.ok("회원가입 성공", result);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refresh(@CookieValue("refresh_token") String refreshToken) {
        String result = authService.refreshAccessToken(refreshToken);

        return ResponseFactory.ok("토큰 리프레쉬 성공", result);
    }
}
