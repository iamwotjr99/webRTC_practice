package com.practice.webRTC.room.infrastructure.jpa;

import com.practice.webRTC.room.domain.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRoomRepository extends JpaRepository<RoomEntity, Long> {

}
