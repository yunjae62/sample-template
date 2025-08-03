package ex.sample.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import ex.sample.global.inmemory.InMemoryStore;
import ex.sample.global.response.ApiResponse;
import ex.sample.global.security.jwt.JwtBearerUtils;
import ex.sample.global.security.jwt.JwtConfig;
import ex.sample.global.security.jwt.JwtTokenFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final InMemoryStore inMemoryStore;
    private final JwtTokenFactory jwtTokenFactory;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {
        log.info("success auth loginId : {}", authentication.getName());

        this.getResponseDtoWithTokensInHeader(authentication, response); // 헤더에서 토큰을 추가한 응답 DTO 생성

        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // JSON 설정
        response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // UTF8 설정하여 한글 표시

        String result = objectMapper.writeValueAsString(ApiResponse.success()); // JSON to String 변환
        response.getWriter().write(result);
    }

    private void getResponseDtoWithTokensInHeader(Authentication authentication, HttpServletResponse response) {
        String name = authentication.getName();
        String role = List.of(authentication.getAuthorities()).getFirst().toString();

        String accessToken = jwtTokenFactory.createAccessToken(name, role);
        String refreshToken = jwtTokenFactory.createRefreshToken(name, role);

        response.addHeader(JwtConfig.ACCESS_TOKEN_HEADER, JwtBearerUtils.addPrefix(accessToken));
        response.addHeader(JwtConfig.REFRESH_TOKEN_HEADER, JwtBearerUtils.addPrefix(refreshToken));

        inMemoryStore.put(name, accessToken, JwtTokenFactory.REFRESH_TOKEN_TTL);
    }
}
