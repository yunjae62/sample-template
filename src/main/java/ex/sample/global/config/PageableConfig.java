package ex.sample.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PageableConfig {

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customizerPageable() {
        return resolver -> {
            resolver.setMaxPageSize(100); // 최대 페이지 사이즈 설정
            resolver.setFallbackPageable(Pageable.ofSize(20)); // 기본 페이지 사이즈 설정
//            resolver.setOneIndexedParameters(true); // 페이지 번호를 1부터 시작하도록 설정
        };
    }
}
