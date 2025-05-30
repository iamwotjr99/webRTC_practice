package com.practice.webRTC.realtime.signaling.application;

import com.practice.webRTC.realtime.signaling.application.dto.SignalingMessageDto;

public interface SignalingService {
    void signaling(Long roomId, String accessToken, SignalingMessageDto message);
}
