package com.practice.webRTC.room.infrastructure.jpa;

import com.practice.webRTC.room.domain.entity.RoomUserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaRoomUserRepository extends JpaRepository<RoomUserEntity, Long> {

    Optional<RoomUserEntity> findByUser_IdAndRoom_Id(Long userId, Long roomId);

    List<RoomUserEntity> findByUser_Id(Long userId);

    @Query("SELECT rue FROM RoomUserEntity rue ORDER BY rue.joinedAt DESC")
    List<RoomUserEntity> findByUser_IdOrderByJoinedAtDesc(Long userId, Pageable pageable);

    boolean existsByUser_IdAndRoom_Id(Long userId, Long roomId);

}
