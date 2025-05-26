package com.practice.webRTC.signaling.application;

import com.practice.webRTC.signaling.application.dto.SignalingMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignalingServiceImpl implements SignalingService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void signaling(Long roomId, Long userId, SignalingMessageDto message) {

    }
}
