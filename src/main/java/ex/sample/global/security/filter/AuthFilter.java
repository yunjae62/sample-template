package ex.sample.global.security.filter;

import ex.sample.global.security.authentication.AccessTokenAuthentication;
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

@Slf4j(topic = "AuthFilter")
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String authenticationHeader = request.getHeader(JwtConfig.ACCESS_TOKEN_HEADER);
        log.info("Access-Token : {}", authenticationHeader);

        // 토큰이 없으면 인증 처리 없이 다음 필터로 전달
        if (!StringUtils.hasText(authenticationHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication preAuthentication = new AccessTokenAuthentication(authenticationHeader);
        Authentication postAuthentication = authenticationManager.authenticate(preAuthentication);
        SecurityContextHolder.getContext().setAuthentication(postAuthentication);

        filterChain.doFilter(request, response);
    }
}