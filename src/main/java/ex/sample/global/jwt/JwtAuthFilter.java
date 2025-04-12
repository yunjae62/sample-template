package ex.sample.global.jwt;

import ex.sample.global.exception.GlobalException;
import ex.sample.global.redis.RedisUtil;
import ex.sample.global.response.ErrorCase;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT 검증하는 인증 필터
 */
@Slf4j(topic = "JwtAuthFilter")
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String accessToken = jwtUtil.getTokenWithoutBearer(request.getHeader(JwtUtil.ACCESS_TOKEN_HEADER));
        log.info("accessToken : {}", accessToken);

        // access token 비어있으면 인증 미처리 후 필터 넘기기
        if (!StringUtils.hasText(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        JwtStatus accessTokenStatus = jwtUtil.validateToken(accessToken);

        switch (accessTokenStatus) {
            case VALID -> authenticateLoginUser(accessToken);
            case EXPIRED -> authenticateWithRefreshToken(request, response);
            case INVALID -> throw new GlobalException(ErrorCase.INVALID_TOKEN);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 로그아웃 여부 확인 후 인증 처리
     */
    private void authenticateLoginUser(String accessToken) {

        String nickname = jwtUtil.getNicknameFromToken(accessToken);

        // 로그아웃 여부 체크
        if (redisUtil.isUserLogout(nickname)) {
            throw new GlobalException(ErrorCase.LOGIN_REQUIRED);
        }

        setAuthentication(accessToken); // 인증 처리
    }

    private void authenticateWithRefreshToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = jwtUtil.getRefreshTokenFromCookies(request.getCookies());

        if (refreshToken == null) {
            throw new GlobalException(ErrorCase.LOGIN_REQUIRED);
        }

        JwtStatus refreshTokenStatus = jwtUtil.validateToken(refreshToken);

        switch (refreshTokenStatus) {
            case VALID -> setAuthWithRenewAccessToken(response, refreshToken);
            case EXPIRED -> throw new GlobalException(ErrorCase.LOGIN_REQUIRED);
            case INVALID -> throw new GlobalException(ErrorCase.INVALID_TOKEN);
        }
    }

    /**
     * 재발급한 토큰들을 추가 후 인증 처리
     */
    private void setAuthWithRenewAccessToken(HttpServletResponse response, String refreshToken) {

        // 요청 리프레쉬 토큰과 레디스의 리프레쉬 토큰이 같은지 검증
        validateSameRefreshToken(refreshToken);

        String nickname = jwtUtil.getNicknameFromToken(refreshToken);
        String role = jwtUtil.getRoleFromToken(refreshToken);

        // Access token & Refresh token 재발급
        String newAccessToken = jwtUtil.createAccessToken(nickname, role);
        String newRefreshToken = jwtUtil.createRefreshToken(nickname, role);

        // Refresh token 담은 쿠키 생성
        Cookie refreshTokenCookie = jwtUtil.createRefreshTokenCookie(newRefreshToken);

        // 레디스에 새로 발급한 Refresh token 저장
        redisUtil.setUserLogin(nickname, newRefreshToken);

        // 응답 객체에 담기
        response.addHeader(JwtUtil.ACCESS_TOKEN_HEADER, jwtUtil.setTokenWithBearer(newAccessToken));
        response.addCookie(refreshTokenCookie);

        // 인증 처리
        setAuthentication(refreshToken);
    }

    private void validateSameRefreshToken(String refreshToken) {

        String nickname = jwtUtil.getNicknameFromToken(refreshToken);
        String refreshTokenInRedis = redisUtil.getUserRefreshToken(nickname);

        if (isRefreshTokenNotSame(refreshToken, refreshTokenInRedis)) {
            log.error("[서로 다른 리프레쉬 토큰] 닉네임 : {}", nickname);
            redisUtil.setUserLogout(nickname);
            throw new GlobalException(ErrorCase.LOGIN_REQUIRED);
        }
    }

    private boolean isRefreshTokenNotSame(String refreshToken, String refreshTokenInRedis) {
        return StringUtils.hasText(refreshTokenInRedis) && !refreshToken.equals(refreshTokenInRedis);
    }

    /**
     * 인증 처리
     */
    private void setAuthentication(String token) {
        String loginId = jwtUtil.getNicknameFromToken(token);
        SecurityContextHolder.getContext().setAuthentication(createAuthentication(loginId));
    }

    /**
     * 인증 객체 생성
     */
    private Authentication createAuthentication(String loginId) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginId);
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }
}
