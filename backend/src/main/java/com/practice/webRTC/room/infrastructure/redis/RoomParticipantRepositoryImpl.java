package com.practice.webRTC.room.infrastructure.redis;

import com.practice.webRTC.room.domain.repository.RoomParticipantRepository;
import java.time.Duration;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RoomParticipantRepositoryImpl implements RoomParticipantRepository {

    // 참가자 수(Count) 관리
    // 참가자(id) 관리

    private static final Duration TTL = Duration.ofHours(24);

    private final RedisTemplate<String, String> redisTemplate;

    private String getCountKey(Long roomId) {
        return "room:participant:" + roomId;
    }

    private String getSetKey(Long roomId) {
        return "room:" + roomId + ":participants";
    }

    @Override
    public int getParticipantCount(Long roomId) {
        String count = redisTemplate.opsForValue().get("room:participant:" + roomId);
        return count == null ? 0 : Integer.parseInt(count);
    }

    @Override
    public void setParticipantCount(Long roomId, int count) {
        if (roomId == null) {
            log.warn("null ID로 Redis 저장 시도");
            return;
        }
        String key = getCountKey(roomId);

        redisTemplate.opsForValue().set(key, String.valueOf(count));
        redisTemplate.expire(key, TTL);
    }

    @Override
    public Set<String> getParticipantIds(Long roomId) {
        return redisTemplate.opsForSet().members(getSetKey(roomId));
    }

    @Override
    public void setParticipant(Long roomId, Long userId) {
        String setKey = getSetKey(roomId);
        String countKey = getCountKey(roomId);

        redisTemplate.opsForSet().add(setKey, String.valueOf(userId));
        redisTemplate.opsForValue().increment(countKey);

        redisTemplate.expire(setKey, TTL);
        redisTemplate.expire(countKey, TTL);
    }

    @Override
    public void removeParticipant(Long roomId, Long userId) {
        redisTemplate.opsForSet().remove(getSetKey(roomId), String.valueOf(userId));

        redisTemplate.opsForValue().decrement(getCountKey(roomId));
    }
}
