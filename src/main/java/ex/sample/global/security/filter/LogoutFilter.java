package ex.sample.global.security.filter;

import ex.sample.global.inmemory.InMemoryStore;
import ex.sample.global.security.WebSecurityConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class LogoutFilter extends OncePerRequestFilter {

    private final InMemoryStore inMemoryStore;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            inMemoryStore.remove(username);
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Logged out successfully");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // "/users/logout" URL이 아니라면 필터를 건너뜁니다.
        return !request.getServletPath().equals(WebSecurityConfig.LOGOUT_URL);
    }
}
