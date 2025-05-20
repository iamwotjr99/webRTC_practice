package com.practice.webRTC.Room.infrastructure.redis;

public interface RoomParticipantRepository {
    int getParticipantCount(Long roomId);
    void setParticipantCount(Long roomId, int count);
}
