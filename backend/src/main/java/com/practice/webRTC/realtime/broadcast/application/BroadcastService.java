package com.practice.webRTC.realtime.broadcast.application;

public interface BroadcastService {
    void broadcastParticipant(Long roomId, String accessToken);
    void broadcastLeave(Long roomId, String accessToken);
}
