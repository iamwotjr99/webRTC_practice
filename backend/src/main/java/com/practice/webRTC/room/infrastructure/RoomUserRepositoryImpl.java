package com.practice.webRTC.room.infrastructure;

import com.practice.webRTC.global.exception.CustomException;
import com.practice.webRTC.global.exception.ErrorCode;
import com.practice.webRTC.room.domain.RoomUser;
import com.practice.webRTC.room.domain.entity.RoomEntity;
import com.practice.webRTC.room.domain.entity.RoomUserEntity;
import com.practice.webRTC.room.domain.repository.RoomUserRepository;
import com.practice.webRTC.room.infrastructure.jpa.JpaRoomRepository;
import com.practice.webRTC.room.infrastructure.jpa.JpaRoomUserRepository;
import com.practice.webRTC.user.domain.entity.UserEntity;
import com.practice.webRTC.user.infrastructure.jpa.JpaUserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoomUserRepositoryImpl implements RoomUserRepository {

    private final JpaRoomUserRepository jpaRoomUserRepository;
    private final JpaRoomRepository jpaRoomRepository;
    private final JpaUserRepository jpaUserRepository;

    @Transactional
    @Override
    public RoomUser save(RoomUser roomUser) {
        UserEntity userEntity = jpaUserRepository.findById(roomUser.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_IS_NOT_FOUND));
        RoomEntity roomEntity = jpaRoomRepository.findById(roomUser.getRoomId())
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_IS_NOT_FOUND));

        Optional<RoomUserEntity> existing = jpaRoomUserRepository.findByUser_IdAndRoom_Id(
                userEntity.getId(), roomEntity.getId());

        if (existing.isPresent()) {
            RoomUserEntity existedEntity = existing.get();
            
            existedEntity.applyDomain(roomUser);

            return existedEntity.toDomain();
        }

        RoomUserEntity roomUserEntity = new RoomUserEntity(userEntity, roomEntity);
        RoomUserEntity savedNewEntity = jpaRoomUserRepository.save(roomUserEntity);
        return savedNewEntity.toDomain();
    }

    // 유저-방 매핑 엔티티 단건 조회
    @Override
    public RoomUser findByUserIdAndRoomId(Long userId, Long roomId) {
        return jpaRoomUserRepository.findByUser_IdAndRoom_Id(userId, roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_USER_IS_NOT_FOUND))
                .toDomain();
    }

    // 유저가 속한 모든 방 조회
    @Override
    public List<RoomUser> findByUserId(Long userId) {
        return jpaRoomUserRepository.findByUser_Id(userId)
                .stream()
                .map(RoomUserEntity::toDomain).toList();
    }

    // 유저가 최근에 참여한 방 순서로 조회
    @Override
    public List<RoomUser> findByUserIdOrderByJoinedAtDesc(Long userId) {
        Pageable pageable = PageRequest.of(0, 5);
        return jpaRoomUserRepository.findByUser_IdOrderByJoinedAtDesc(userId, pageable)
                .stream()
                .map(RoomUserEntity::toDomain).toList();
    }

    @Override
    public boolean existByUserIdAndRoomId(Long userId, Long roomId) {
        return jpaRoomUserRepository.existsByUser_IdAndRoom_Id(userId, roomId);
    }
}
