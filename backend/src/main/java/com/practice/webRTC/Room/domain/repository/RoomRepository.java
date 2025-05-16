package com.practice.webRTC.Room.domain.repository;

import com.practice.webRTC.Room.domain.Room;
import java.util.List;

public interface RoomRepository {
    Room save(Room room);

    Room findById(Long roomId);

    List<Room> findAll();
}
