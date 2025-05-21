package com.practice.webRTC.auth.infrastructure.security.userdetails;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

// CustomUserDetails 는 불변성을 가져야한다.
// securityFilter 에서 JwtAuthenticationFilter 에서 토큰에 있는 유저의 정보를
// CustomUserDetails 에 넣어서 사용하기 때문에, 유저의 정보가 하나의 요청 내에서는 바뀔일이 없기 때문에
// 불변성을 가져야한다.
public record CustomUserDetails(Long id, String nickname) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return nickname;
    }
}
