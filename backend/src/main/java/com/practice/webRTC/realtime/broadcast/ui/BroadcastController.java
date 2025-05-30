package com.practice.webRTC.realtime.broadcast.ui;

import com.practice.webRTC.realtime.broadcast.application.BroadcastService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BroadcastController {

    private final BroadcastService broadcastService;

    @MessageMapping("/room/{roomId}/participants")
    public void broadcastParticipant(@DestinationVariable Long roomId, Message<?> rawMessage) {
        SimpMessageHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(rawMessage, SimpMessageHeaderAccessor.class);

        Map<String, Object> sessionAttribute = accessor.getSessionAttributes();

        String accessToken = (String) sessionAttribute.get("accessToken");

        broadcastService.broadcastParticipant(roomId, accessToken);
    }

    @MessageMapping("/room/{roomId}/leave")
    public void broadcastLeave(@DestinationVariable Long roomId, Message<?> rawMessage) {
        SimpMessageHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(rawMessage, SimpMessageHeaderAccessor.class);

        Map<String, Object> sessionAttribute = accessor.getSessionAttributes();

        String accessToken = (String) sessionAttribute.get("accessToken");

        broadcastService.broadcastLeave(roomId, accessToken);
    }
}
