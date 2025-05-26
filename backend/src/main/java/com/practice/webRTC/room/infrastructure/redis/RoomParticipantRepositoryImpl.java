package com.practice.webRTC.room.infrastructure.redis;

import com.practice.webRTC.room.domain.repository.RoomParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoomParticipantRepositoryImpl implements RoomParticipantRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public int getParticipantCount(Long roomId) {
        String count = redisTemplate.opsForValue().get("room:participant:" + roomId);
        return count == null ? 0 : Integer.parseInt(count);
    }

    @Override
    public void setParticipantCount(Long roomId, int count) {
        redisTemplate.opsForValue().set("room:participant:" + roomId, String.valueOf(count));
    }
}
