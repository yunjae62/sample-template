package ex.sample.global.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import ex.sample.global.security.filter.AuthFilter;
import ex.sample.global.security.filter.ExceptionFilter;
import ex.sample.global.security.filter.LoginFilter;
import ex.sample.global.security.filter.LogoutFilter;
import ex.sample.global.security.filter.RefreshFilter;
import ex.sample.global.security.handler.RefreshSuccessHandler;
import ex.sample.infra.inmemory.InMemoryStore;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final ObjectMapper objectMapper;
    private final InMemoryStore inMemoryStore;
    private final RefreshSuccessHandler refreshSuccessHandler;
    private final AuthenticationSuccessHandler loginSuccessHandler;

    @Bean
    public LoginFilter loginFilter(AuthenticationManager authenticationManager) {
        LoginFilter filter = new LoginFilter(objectMapper, loginSuccessHandler);
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Bean
    public LogoutFilter logoutFilter() {
        return new LogoutFilter(inMemoryStore);
    }

    @Bean
    public AuthFilter authFilter(AuthenticationManager authenticationManager) {
        return new AuthFilter(authenticationManager);
    }

    @Bean
    public RefreshFilter refreshFilter(AuthenticationManager authenticationManager) {
        return new RefreshFilter(refreshSuccessHandler, authenticationManager);
    }

    @Bean
    public ExceptionFilter exceptionFilter() {
        return new ExceptionFilter(objectMapper);
    }
}
