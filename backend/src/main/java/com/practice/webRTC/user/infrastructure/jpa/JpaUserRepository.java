package com.practice.webRTC.user.infrastructure.jpa;

import com.practice.webRTC.user.domain.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByNicknameEntity_Nickname(String nickname);

    Optional<UserEntity> findByNicknameEntity_Nickname(String nickname);
}
