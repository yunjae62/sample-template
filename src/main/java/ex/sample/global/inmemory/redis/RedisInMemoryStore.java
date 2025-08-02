package ex.sample.global.inmemory.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ex.sample.global.exception.GlobalException;
import ex.sample.global.inmemory.InMemoryStore;
import ex.sample.global.response.ResponseCode;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisInMemoryStore implements InMemoryStore {

    private final ObjectMapper mapper;
    private final StringRedisTemplate redisTemplate;

    @Override
    public <T> void put(String key, T value) {
        put(key, value, null);
    }

    @Override
    public <T> void put(String key, T value, Duration ttl) {
        String json = toJson(value);

        if (ttl != null) {
            redisTemplate.opsForValue().set(key, json, ttl);
        } else {
            redisTemplate.opsForValue().set(key, json);
        }
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> clazz) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
            .map(json -> parseJson(json, clazz));
    }

    @Override
    public boolean containsKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void clear() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().commands().flushAll();
    }

    private <T> String toJson(T value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("Redis serialization error: {}", value, e);
            throw new GlobalException(ResponseCode.SYSTEM_ERROR);
        }
    }

    private <T> T parseJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Redis deserialization error: {}", json, e);
            throw new GlobalException(ResponseCode.SYSTEM_ERROR);
        }
    }
}
