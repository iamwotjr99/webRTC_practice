package com.practice.webRTC.Room.infrastructure;

import com.practice.webRTC.Room.infrastructure.redis.RoomParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoomParticipantRepositoryImpl implements RoomParticipantRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public int getParticipantCount(Long roomId) {
        String countStr = redisTemplate.opsForValue().get("room:participant:" + roomId);
        return Integer.parseInt(countStr);
    }

    @Override
    public void setParticipantCount(Long roomId, int count) {
        redisTemplate.opsForValue().set("room:participant:" + roomId, String.valueOf(count));
    }
}
