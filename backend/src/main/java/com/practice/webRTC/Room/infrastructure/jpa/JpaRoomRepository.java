package com.practice.webRTC.Room.infrastructure.jpa;

import com.practice.webRTC.Room.domain.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRoomRepository extends JpaRepository<RoomEntity, Long> {

}
