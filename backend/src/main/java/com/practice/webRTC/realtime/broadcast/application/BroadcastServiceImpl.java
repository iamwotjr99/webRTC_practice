package com.practice.webRTC.realtime.broadcast.application;

import com.practice.webRTC.auth.infrastructure.jwt.JwtProvider;
import com.practice.webRTC.realtime.broadcast.ui.dto.ParticipantsResDto;
import com.practice.webRTC.room.domain.repository.RoomParticipantRepository;
import com.practice.webRTC.user.domain.User;
import com.practice.webRTC.user.domain.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public void broadcastParticipant(Long roomId, String accessToken) {
        Set<String> allParticipantIds = roomParticipantRepository.getParticipantIds(roomId);

        List<Long> participantIds = allParticipantIds.stream()
                .map(Long::valueOf)
                .toList();

        List<User> participants = userRepository.findAllById(participantIds);

        List<ParticipantsResDto> participantsResDtos = participants.stream()
                .map(user -> new ParticipantsResDto(user.getId(), user.getNicknameValue()))
                .toList();

        simpMessagingTemplate.convertAndSend("/sub/room/" + roomId + "/participants", participantsResDtos);
    }

    @Override
    public void broadcastLeave(Long roomId, String accessToken) {
        Long userId = jwtProvider.getUserIdFromToken(accessToken);

        System.out.println("userId = " + userId);

        simpMessagingTemplate.convertAndSend("/sub/room/" + roomId + "/leave", userId);
    }
}
