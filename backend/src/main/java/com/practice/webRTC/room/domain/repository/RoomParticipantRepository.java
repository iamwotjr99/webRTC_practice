package com.practice.webRTC.room.domain.repository;

public interface RoomParticipantRepository {
    int getParticipantCount(Long roomId);
    void setParticipantCount(Long roomId, int count);
}
