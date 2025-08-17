package ex.sample.global.security.config;

import ex.sample.global.security.filter.AuthFilter;
import ex.sample.global.security.filter.ExceptionFilter;
import ex.sample.global.security.filter.LoginFilter;
import ex.sample.global.security.filter.LogoutFilter;
import ex.sample.global.security.filter.RefreshFilter;
import ex.sample.global.security.jwt.JwtConfig;
import java.util.List;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        LoginFilter loginFilter,
        LogoutFilter logoutFilter,
        AuthFilter authFilter,
        RefreshFilter refreshFilter,
        ExceptionFilter exceptionFilter
    ) throws Exception {
        // CSRF 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // 세션 비활성화
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // CORS 설정
        setCors(http);

        // Filter 순서 설정
        http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(logoutFilter, LoginFilter.class);
        http.addFilterBefore(authFilter, LogoutFilter.class);
        http.addFilterBefore(refreshFilter, AuthFilter.class);
        http.addFilterBefore(exceptionFilter, RefreshFilter.class);

        // URL 권한 설정
        setAuthRequest(http);

        return http.build();
    }

    private void setCors(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(CorsConstant.ALLOWED_ORIGINS);
            config.setAllowedMethods(CorsConstant.ALLOWED_METHODS);
            config.setAllowedHeaders(List.of("*"));
            config.setExposedHeaders(List.of(JwtConfig.REFRESH_TOKEN_HEADER));
            return config;
        }));
    }

    private void setAuthRequest(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz ->
            authz
                // 정적 파일
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                // Swagger UI
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs.yaml").permitAll()
                // Sample 도메인
                .requestMatchers("/samples/**").permitAll()
                // 인증
                .requestMatchers(HttpMethod.POST, UrlConstant.SIGNUP_URL, UrlConstant.REFRESH_URL).permitAll()
                // 그 외
                .anyRequest().authenticated()
        );
    }
}