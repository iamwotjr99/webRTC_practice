package com.practice.webRTC.realtime.broadcast.application;

import com.practice.webRTC.auth.infrastructure.jwt.JwtProvider;
import com.practice.webRTC.room.domain.repository.RoomParticipantRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BroadcastServiceImpl implements BroadcastService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RoomParticipantRepository roomParticipantRepository;
    private final JwtProvider jwtProvider;

    @Override
    public void broadcastParticipant(Long roomId, String accessToken) {
        Long userId = jwtProvider.getUserIdFromToken(accessToken);

        Set<String> allParticipantIds = roomParticipantRepository.getParticipantIds(roomId);

        List<Long> participantIds = allParticipantIds.stream()
                .map(Long::valueOf)
                .toList();

        simpMessagingTemplate.convertAndSend("/sub/room/" + roomId + "/participants", participantIds);
    }

    @Override
    public void broadcastLeave(Long roomId, String accessToken) {
        Long userId = jwtProvider.getUserIdFromToken(accessToken);

        System.out.println("userId = " + userId);

        simpMessagingTemplate.convertAndSend("/sub/room/" + roomId + "/leave", userId);
    }
}
