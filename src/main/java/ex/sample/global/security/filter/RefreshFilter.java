package ex.sample.global.security.filter;

import ex.sample.global.security.WebSecurityConfig;
import ex.sample.global.security.authentication.RefreshTokenAuthentication;
import ex.sample.global.security.handler.RefreshSuccessHandler;
import ex.sample.global.security.jwt.JwtConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "RefreshFilter")
@RequiredArgsConstructor
public class RefreshFilter extends OncePerRequestFilter {

    private final RefreshSuccessHandler refreshSuccessHandler;
    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String authenticationHeader = request.getHeader(JwtConfig.REFRESH_TOKEN_HEADER);
        log.info("Refresh-Token : {}", authenticationHeader);

        // 토큰이 없으면 인증 처리 없이 다음 필터로 전달
        if (!StringUtils.hasText(authenticationHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication preAuthentication = new RefreshTokenAuthentication(authenticationHeader);
        Authentication postAuthentication = authenticationManager.authenticate(preAuthentication);
        SecurityContextHolder.getContext().setAuthentication(postAuthentication);

        refreshSuccessHandler.onAuthenticationSuccess(response, postAuthentication);

//        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // "/users/refresh" 경로가 아니라면 필터 로직을 수행하지 않음
        return !request.getServletPath().equals(WebSecurityConfig.REFRESH_URL);
    }
}