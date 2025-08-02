package ex.sample.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import ex.sample.global.inmemory.redis.RedisUtil;
import ex.sample.global.security.filter.AuthFilter;
import ex.sample.global.security.filter.ExceptionFilter;
import ex.sample.global.security.filter.LoginFilter;
import ex.sample.global.security.filter.LogoutFilter;
import ex.sample.global.security.filter.RefreshFilter;
import ex.sample.global.security.handler.RefreshSuccessHandler;
import ex.sample.global.security.jwt.JwtConfig;
import ex.sample.global.security.provider.AccessTokenAuthenticationProvider;
import ex.sample.global.security.provider.RefreshTokenAuthenticationProvider;
import ex.sample.global.security.provider.UsernamePasswordAuthenticationProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    public static final String SIGNUP_URL = "/users/signup";
    public static final String LOGIN_URL = "/users/login";
    public static final String LOGOUT_URL = "/users/logout";
    public static final String REFRESH_URL = "/users/refresh";

    private static final List<String> ALLOWED_ORIGINS = List.of(
        "http://localhost:3000"
    );

    private static final List<String> ALLOWED_METHODS = List.of(
        HttpMethod.GET.name(),
        HttpMethod.POST.name(),
        HttpMethod.PUT.name(),
        HttpMethod.PATCH.name(),
        HttpMethod.DELETE.name(),
        HttpMethod.OPTIONS.name()
    );

    private final AccessTokenAuthenticationProvider accessTokenAuthProvider;
    private final RefreshTokenAuthenticationProvider refreshTokenAuthProvider;
    private final UsernamePasswordAuthenticationProvider usernamePasswordAuthProvider;

    /**
     * 비밀번호 암호화 설정 (BCrypt)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(refreshTokenAuthProvider, accessTokenAuthProvider, usernamePasswordAuthProvider);
    }

    @Bean
    public RefreshFilter refreshFilter(RefreshSuccessHandler refreshSuccessHandler) {
        return new RefreshFilter(refreshSuccessHandler, authenticationManager());
    }

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter(authenticationManager());
    }

    @Bean
    public LoginFilter loginFilter(ObjectMapper objectMapper, AuthenticationSuccessHandler loginSuccessHandler) {
        LoginFilter filter = new LoginFilter(objectMapper, loginSuccessHandler);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean
    public LogoutFilter logoutFilter(RedisUtil redisUtil) {
        return new LogoutFilter(redisUtil);
    }

    @Bean
    public ExceptionFilter exceptionFilter(ObjectMapper objectMapper) {
        return new ExceptionFilter(objectMapper);
    }

    @Bean
    public AuthFilter jwtAuthFilter() {
        return new AuthFilter(authenticationManager());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        LoginFilter loginFilter,
        LogoutFilter logoutFilter,
        AuthFilter authFilter,
        RefreshFilter refreshFilter,
        ExceptionFilter exceptionFilter
    ) throws Exception {

        // CSRF 비활성화 설정
        http.csrf(AbstractHttpConfigurer::disable);

        // CORS 설정
        http.cors(getCorsConfigurerCustomizer());

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Filter 순서 설정
        http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(logoutFilter, LoginFilter.class);
        http.addFilterBefore(authFilter, LogoutFilter.class);
        http.addFilterBefore(refreshFilter, AuthFilter.class);
        http.addFilterBefore(exceptionFilter, RefreshFilter.class);

        // 요청 URL 접근 설정
        settingRequestAuthorization(http);

        return http.build();
    }

    /**
     * CORS 설정
     */
    private Customizer<CorsConfigurer<HttpSecurity>> getCorsConfigurerCustomizer() {
        return corsConfigurer -> corsConfigurer.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(ALLOWED_ORIGINS);
            config.setAllowedMethods(ALLOWED_METHODS);
            config.setAllowedHeaders(List.of("")); // preflight 요청에 대한 응답 헤더 허용
            config.setExposedHeaders(List.of(JwtConfig.REFRESH_TOKEN_HEADER)); // 브라우저가 접근할 수 있는 응답 헤더 허용
            return config;
        });
    }

    /**
     * 요청 URL 접근 설정
     */
    private void settingRequestAuthorization(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz ->
            authz
                // 정적 파일
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                // Swagger UI
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs.yaml").permitAll()
                // Sample 도메인
                .requestMatchers("/samples/**").permitAll()
                // 인증
                .requestMatchers(HttpMethod.POST, SIGNUP_URL, REFRESH_URL).permitAll()
                // 그 외
                .anyRequest().authenticated()
        );
    }
}

