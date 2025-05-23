package com.practice.webRTC.user.ui;

import com.practice.webRTC.auth.infrastructure.security.userdetails.CustomUserDetails;
import com.practice.webRTC.global.common.ApiResponse;
import com.practice.webRTC.global.common.ResponseFactory;
import com.practice.webRTC.user.application.UserService;
import com.practice.webRTC.user.application.dto.GetUserInfoResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<GetUserInfoResDto>> getUserInfo(@AuthenticationPrincipal
            CustomUserDetails userDetails) {
        GetUserInfoResDto result = userService.getUserInfoById(userDetails.id());

        return ResponseFactory.ok("유저 정보 조회 성공", result);
    }
}
