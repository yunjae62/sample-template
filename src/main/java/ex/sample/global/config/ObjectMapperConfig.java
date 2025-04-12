package ex.sample.global.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Java 8 날짜 지원
        mapper.registerModule(new JavaTimeModule());

        // 날짜 포맷: timestamp 비활성화
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // null 필드 제외 (선택 사항)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 알 수 없는 필드가 있어도 예외 발생하지 않음
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return mapper;
    }
}
