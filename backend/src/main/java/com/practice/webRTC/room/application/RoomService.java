package com.practice.webRTC.room.application;

import com.practice.webRTC.global.exception.CustomException;
import com.practice.webRTC.global.exception.ErrorCode;
import com.practice.webRTC.room.application.dto.CreateRoomReqDto;
import com.practice.webRTC.room.application.dto.EnterRoomResDto;
import com.practice.webRTC.room.application.dto.JoinRoomResDto;
import com.practice.webRTC.room.application.dto.ParticipantInfo;
import com.practice.webRTC.room.domain.Room;
import com.practice.webRTC.room.domain.RoomUser;
import com.practice.webRTC.room.domain.repository.RoomParticipantRepository;
import com.practice.webRTC.room.domain.repository.RoomRepository;
import com.practice.webRTC.room.domain.repository.RoomUserRepository;
import com.practice.webRTC.user.domain.User;
import com.practice.webRTC.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomUserRepository roomUserRepository;
    private final RoomParticipantRepository roomParticipantRepository;
    private final UserRepository userRepository;

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
    public JoinRoomResDto joinRoom(Long userId, Long roomId) {
        boolean alreadyJoined = roomUserRepository.existByUserIdAndRoomId(userId, roomId);

        Room room = roomRepository.findById(roomId);

        if (alreadyJoined) {
            RoomUser roomUser = roomUserRepository.findByUserIdAndRoomId(userId, roomId);
            roomUser.rejoin();
            roomUserRepository.save(roomUser);
        } else {
            if (!room.canJoin()) {
                throw new CustomException(ErrorCode.ROOM_IS_FULL);
            }

            RoomUser roomUser = RoomUser.join(userId, roomId);
            room.addParticipant();

            roomRepository.save(room);
            roomUserRepository.save(roomUser);
        }

        return JoinRoomResDto.from(room);
    }

    // 방 입장
    public EnterRoomResDto enterRoom(Long userId, Long roomId) {
        // Redis에 참가자로 등록
        // 참가자 목록 조회
        // 자기 자신을 제외한 나머지 참가자 전달
        roomParticipantRepository.setParticipant(roomId, userId);

        Set<String> allParticipantIds = roomParticipantRepository.getParticipantIds(roomId);

        List<Long> otherParticipantIds = allParticipantIds.stream()
                .map(Long::valueOf)
                .filter(id -> !id.equals(userId))
                .toList();

        List<User> otherParticipants = userRepository.findAllById(otherParticipantIds);

        List<ParticipantInfo> participantDtos = otherParticipants.stream()
                .map(user -> new ParticipantInfo(user.getId(), user.getNicknameValue()))
                .toList();

        return EnterRoomResDto.from(userId, participantDtos);
    }

    // 방 퇴장( 완전 퇴장 x )
    public void leaveRoom(Long userId, Long roomId) {
        roomParticipantRepository.removeParticipant(roomId, userId);
    }

    // 내가 속한 방 목록 조회
    @Transactional
    public List<Room> getMyRoomList(Long userId) {
        List<RoomUser> roomUserList = roomUserRepository.findByUserId(userId);

        return roomUserList.stream()
                .map((roomUser) ->
                        roomRepository.findById(roomUser.getRoomId())).toList();
    }

    // 최근에 입장한 방 목록 조회
    @Transactional
    public List<Room> getMyRoomListByJoinedAt(Long userId) {
        List<RoomUser> roomUserList = roomUserRepository.findByUserIdOrderByJoinedAtDesc(
                userId);

        return roomUserList.stream()
                .map(RoomUser::getRoomId)
                .distinct()
                .map(roomRepository::findById)
                .toList();
    }
}
