package com.practice.webRTC.user.infrastructure;

import com.practice.webRTC.global.exception.CustomException;
import com.practice.webRTC.global.exception.ErrorCode;
import com.practice.webRTC.user.domain.User;
import com.practice.webRTC.user.domain.entity.UserEntity;
import com.practice.webRTC.user.domain.repository.UserRepository;
import com.practice.webRTC.user.infrastructure.jpa.JpaUserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public User save(User user) {
        UserEntity userEntity = new UserEntity(user);
        UserEntity savedUser = jpaUserRepository.save(userEntity);
        return savedUser.toUser();
    }

    @Override
    public User findById(Long userId) {
        UserEntity userEntity = jpaUserRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_IS_NOT_FOUND));
        return userEntity.toUser();
    }

    @Override
    public User findByNickname(String nickname) {
        UserEntity userEntity = jpaUserRepository.findByNicknameEntity_Nickname(nickname)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_IS_NOT_FOUND));

        return userEntity.toUser();
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return jpaUserRepository.existsByNicknameEntity_Nickname(nickname);
    }

    @Override
    public List<User> findAllById(List<Long> userIds) {
        return jpaUserRepository.findAllById(userIds)
                .stream()
                .map(UserEntity::toUser)
                .toList();
    }
}
