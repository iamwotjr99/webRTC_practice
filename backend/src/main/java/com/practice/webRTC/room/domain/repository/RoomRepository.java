package com.practice.webRTC.room.domain.repository;

import com.practice.webRTC.room.domain.Room;
import java.util.List;

public interface RoomRepository {
    Room save(Room room);

    Room findById(Long roomId);

    List<Room> findAll();

    // Room findByRoomCode(String roomCode);
}
