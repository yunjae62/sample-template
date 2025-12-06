package ex.sample.infra.inmemory.redis;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager redisCacheManager(ObjectMapper objectMapper, RedisConnectionFactory redisConnectionFactory) {

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig() // 기본 설정
            .serializeKeysWith(SerializationPair.fromSerializer(RedisSerializer.string())) // 키는 문자열
            .serializeValuesWith(SerializationPair.fromSerializer(RedisSerializer.json())) // 직렬화
            .entryTtl(Duration.ofSeconds(5L)) // 기본 캐싱 TTL 설정
            .disableCachingNullValues(); // null 캐싱 불가

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(redisCacheConfiguration)
            .build();
    }
}
