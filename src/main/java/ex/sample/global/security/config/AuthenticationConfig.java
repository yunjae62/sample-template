package ex.sample.global.security.config;

import ex.sample.global.security.provider.AccessTokenAuthenticationProvider;
import ex.sample.global.security.provider.RefreshTokenAuthenticationProvider;
import ex.sample.global.security.provider.UsernamePasswordAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;

@Configuration
public class AuthenticationConfig {

    @Bean
    public AuthenticationManager authenticationManager(
        AccessTokenAuthenticationProvider accessTokenAuthProvider,
        RefreshTokenAuthenticationProvider refreshTokenAuthProvider,
        UsernamePasswordAuthenticationProvider usernamePasswordAuthProvider
    ) {
        return new ProviderManager(refreshTokenAuthProvider, accessTokenAuthProvider, usernamePasswordAuthProvider);
    }
}
