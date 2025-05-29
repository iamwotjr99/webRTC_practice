package com.practice.webRTC.room.domain.repository;

import java.util.Set;

public interface RoomParticipantRepository {
    int getParticipantCount(Long roomId);
    void setParticipantCount(Long roomId, int count);

    Set<String> getParticipantIds(Long roomId);
    void setParticipant(Long roomId, Long userId);

    void removeParticipant(Long roomId, Long userId);
}
