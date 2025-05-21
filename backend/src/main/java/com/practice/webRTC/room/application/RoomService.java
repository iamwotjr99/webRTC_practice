package com.practice.webRTC.room.application;

import com.practice.webRTC.room.application.dto.CreateRoomReqDto;
import com.practice.webRTC.room.domain.Room;
import com.practice.webRTC.room.domain.repository.RoomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public Room createRoom(CreateRoomReqDto dto) {
        Room room = Room.createRoom(dto.userId(), dto.title(), dto.capacity());
        return roomRepository.save(room);
    }

    public Room getRoom(Long roomId) {
        return roomRepository.findById(roomId);
    }

    // 유저 도메인 구현 이후, 유저-방 매핑 테이블 구현 후 삭제하고 다른 방목록 조회 함수로 대체 예정
    public List<Room> getAllRoom() {
        return roomRepository.findAll();
    }
}
