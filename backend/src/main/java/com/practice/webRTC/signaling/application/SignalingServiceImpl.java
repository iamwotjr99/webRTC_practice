package com.practice.webRTC.signaling.application;

import com.practice.webRTC.auth.infrastructure.jwt.JwtProvider;
import com.practice.webRTC.global.exception.CustomException;
import com.practice.webRTC.global.exception.ErrorCode;
import com.practice.webRTC.signaling.application.dto.SignalingMessageDto;
import com.practice.webRTC.user.domain.User;
import com.practice.webRTC.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignalingServiceImpl implements SignalingService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public void signaling(Long roomId, String accessToken, SignalingMessageDto message) {
        // TODO: offer 유저 인증/검증 로직 처리

        SignalType type = message.getType();

        if (accessToken == null || accessToken.isBlank()) {
            throw new CustomException(ErrorCode.TOKEN_IS_NOT_FOUND);
        }

        if (!jwtProvider.validateToken(accessToken)) {
            throw new CustomException(ErrorCode.TOKEN_INVALID);
        }

        Long userId = jwtProvider.getUserIdFromToken(accessToken);
        User user = userRepository.findById(userId);

        if (!user.getId().equals(message.senderId())) {
            throw new CustomException(ErrorCode.USER_UNAUTHORIZED);
        }

        switch (type) {
            case OFFER -> handleOffer(roomId, message);
            case ANSWER -> handleAnswer(roomId, message);
            case CANDIDATE -> handleCandidate(roomId, message);
            default -> throw new CustomException(ErrorCode.SIGNALING_UNKNOWN_TYPE);
        }
    }

    private void handleOffer(Long roomId, SignalingMessageDto message) {
        simpMessagingTemplate.convertAndSend("/sub/signal/" + roomId, message);
    }

    private void handleAnswer(Long roomId, SignalingMessageDto message) {
        simpMessagingTemplate.convertAndSend("/sub/signal/" + roomId, message);
    }

    private void handleCandidate(Long roomId, SignalingMessageDto message) {
        simpMessagingTemplate.convertAndSend("/sub/signal/" + roomId, message);
    }

    private void sendToTarget(Long roomId, SignalingMessageDto message) {
        String targetDestination = "/sub/signal/" + roomId + "/" + message.targetId();
        simpMessagingTemplate.convertAndSend(targetDestination, message);
    }
}
