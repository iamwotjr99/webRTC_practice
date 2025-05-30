package com.practice.webRTC.signaling.application;

import com.practice.webRTC.signaling.application.dto.SignalingMessageDto;

public interface SignalingService {
    void signaling(Long roomId, String accessToken, SignalingMessageDto message);
}
