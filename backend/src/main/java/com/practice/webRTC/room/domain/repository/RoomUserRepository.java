package com.practice.webRTC.room.domain.repository;

import com.practice.webRTC.room.domain.RoomUser;
import java.util.List;

public interface RoomUserRepository {
    RoomUser save(RoomUser roomUser);
    RoomUser findByUserIdAndRoomId(Long userId, Long roomId);
    List<RoomUser> findByUserId(Long userId);
    List<RoomUser> findByUserIdOrderByJoinedAtDesc(Long userId);
    boolean existByUserIdAndRoomId(Long userId, Long roomId);
}
