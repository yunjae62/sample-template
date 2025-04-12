package ex.sample.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import ex.sample.global.response.CommonRes;
import ex.sample.global.security.jwt.JwtBearerUtils;
import ex.sample.global.security.jwt.JwtConfig;
import ex.sample.global.security.jwt.JwtTokenFactory;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtTokenFactory jwtTokenFactory;

    public void onAuthenticationSuccess(HttpServletResponse response, Authentication authentication) throws IOException {
        this.getResponseDtoWithTokensInHeader(authentication, response); // 헤더에서 토큰을 추가한 응답 DTO 생성

        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // JSON 설정
        response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // UTF8 설정하여 한글 표시

        String result = objectMapper.writeValueAsString(CommonRes.success()); // JSON to String 변환
        response.getWriter().write(result);
    }

    private void getResponseDtoWithTokensInHeader(Authentication authentication, HttpServletResponse response) {
        String name = authentication.getName();
        String role = List.of(authentication.getAuthorities()).getFirst().toString();

        String accessToken = jwtTokenFactory.createAccessToken(name, role);
        String refreshToken = jwtTokenFactory.createRefreshToken(name, role);

        response.addHeader(JwtConfig.ACCESS_TOKEN_HEADER, JwtBearerUtils.addPrefix(accessToken));
        response.addHeader(JwtConfig.REFRESH_TOKEN_HEADER, JwtBearerUtils.addPrefix(refreshToken));

        // redisUtil.set(name, refreshToken, JwtTokenFactory.REFRESH_TOKEN_TTL);
    }
}
