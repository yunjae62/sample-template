package ex.sample.global.security.jwt;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    public static final String ACCESS_TOKEN_HEADER = "Authorization"; // Access Token Header KEY 값
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token"; // Refresh Token Header KEY 값
    public static final String AUTHORIZATION_KEY = "auth"; // JWT 내의 사용자 권한 값의 KEY

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    @Bean
    public SecretKey secretKey() {
        byte[] decoded = Base64.getDecoder().decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(decoded);
    }

    @Bean
    public JwtParser jwtParser(SecretKey secretKey) {
        return Jwts.parser().verifyWith(secretKey).build();
    }
}
