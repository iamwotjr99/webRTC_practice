package com.practice.webRTC.signaling.ui;

import com.practice.webRTC.signaling.application.SignalingService;
import com.practice.webRTC.signaling.application.dto.SignalingMessageDto;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SignalingController {

    private final SignalingService signalingService;

    @MessageMapping("/signal/{roomId}")
    public void signaling(@DestinationVariable Long roomId, @Payload SignalingMessageDto message,
            Message<?> rawMessage) {
        System.out.println("✅ @MessageMapping 수신 완료");
        log.info("[시그널 수신] senderId: {}, message: {}", message.senderId(), message);
        SimpMessageHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(rawMessage,
                SimpMessageHeaderAccessor.class);

        Map<String, Object> sessionAttribute = accessor.getSessionAttributes();

        String accessToken = (String) sessionAttribute.get("accessToken");

        signalingService.signaling(roomId, accessToken, message);
    }
}
