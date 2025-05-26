package com.practice.webRTC.room.application;

import com.practice.webRTC.room.application.dto.CreateRoomReqDto;
import com.practice.webRTC.room.domain.Room;
import com.practice.webRTC.room.domain.RoomUser;
import com.practice.webRTC.room.domain.repository.RoomParticipantRepository;
import com.practice.webRTC.room.domain.repository.RoomRepository;
import com.practice.webRTC.room.domain.repository.RoomUserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomUserRepository roomUserRepository;
    private final RoomParticipantRepository roomParticipantRepository;

    // 방생성하고 자동입장? x

    // 방 만들고 해당 유저(방만든 유저)와 해당 방 매핑
    @Transactional
    public Room createRoom(Long userId, CreateRoomReqDto dto) {
        Room room = Room.createRoom(userId, dto.title(), dto.capacity());
        Room savedRoom = roomRepository.save(room);
        RoomUser roomUser = RoomUser.join(savedRoom.getUserId(), savedRoom.getId());

        roomUserRepository.save(roomUser);

        return savedRoom;
    }

    // 방에 가입 (방에 입장할 자격을 얻는 행위)
    // 실시간 성 입장 (enter)와는 다름
    @Transactional
    public void joinRoom(Long userId, Long roomId) {
        boolean alreadyJoined = roomUserRepository.existByUserIdAndRoomId(userId, roomId);
        Room room = roomRepository.findById(roomId);

        RoomUser roomUser;
        if (!alreadyJoined && room.canJoin()) {
            roomUser = RoomUser.join(userId, roomId);
            room.addParticipant();
        } else {
            roomUser = roomUserRepository.findByUserIdAndRoomId(userId, roomId);
            roomUser.rejoin();
        }

        roomRepository.save(room);
        roomUserRepository.save(roomUser);
    }

    // 내가 속한 방 목록 조회
    @Transactional
    public List<Room> getMyRoomList(Long userId) {
        List<RoomUser> roomUserList = roomUserRepository.findByUserId(userId);
        return roomUserList.stream()
                .map((roomUser) -> roomRepository.findById(roomUser.getRoomId())).toList();
    }

    // 최근에 입장한 방 목록 조회
    @Transactional
    public List<Room> getMyRoomListByJoinedAt(Long userId) {
        List<RoomUser> roomUserList = roomUserRepository.findByUserIdOrderByJoinedAtDesc(
                userId);
        return roomUserList.stream()
                .map((roomUser) -> roomRepository.findById(roomUser.getRoomId())).toList();
    }
}
