package com.practice.webRTC.Room.infrastructure;

import com.practice.webRTC.Room.domain.Room;
import com.practice.webRTC.Room.domain.entity.RoomEntity;
import com.practice.webRTC.Room.domain.repository.RoomRepository;
import com.practice.webRTC.Room.infrastructure.jpa.JpaRoomRepository;
import com.practice.webRTC.Room.infrastructure.redis.RoomParticipantRepository;
import com.practice.webRTC.global.exception.CustomException;
import com.practice.webRTC.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepository {

    private final JpaRoomRepository jpaRoomRepository;
    private final RoomParticipantRepository roomParticipantRepository;

    @Override
    public Room save(Room room) {
        RoomEntity roomEntity = new RoomEntity(room);
        roomParticipantRepository.setParticipantCount(room.getId(), 0);

        int participantCount = roomParticipantRepository.getParticipantCount(room.getId());
        return jpaRoomRepository.save(roomEntity).toRoom(participantCount);
    }

    @Override
    public Room findById(Long roomId) {
        RoomEntity roomEntity = jpaRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_IS_NOT_FOUND));

        int participantCount = roomParticipantRepository.getParticipantCount(roomId);

        return roomEntity.toRoom(participantCount);
    }

    @Override
    public List<Room> findAll() {
        List<RoomEntity> entities = jpaRoomRepository.findAll();

        return entities.stream()
                .map((entity) -> {
                    Long roomId = entity.getId();
                    int participantCount = roomParticipantRepository.getParticipantCount(roomId);
                    return entity.toRoom(participantCount);
                })
                .toList();
    }
}
