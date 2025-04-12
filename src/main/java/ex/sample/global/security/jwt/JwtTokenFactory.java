package ex.sample.global.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenFactory {

    public static final Duration ACCESS_TOKEN_TTL = Duration.ofMinutes(30); // Access token 만료 시간
    public static final Duration REFRESH_TOKEN_TTL = Duration.ofDays(14); // Refresh token 만료 시간

    private final SecretKey secretKey;

    /**
     * Access Token 생성
     */
    public String createAccessToken(String subject, String role) {
        return createToken(subject, role, ACCESS_TOKEN_TTL);
    }

    /**
     * Refresh Token 생성
     */
    public String createRefreshToken(String subject, String role) {
        return createToken(subject, role, REFRESH_TOKEN_TTL);
    }

    private String createToken(String subject, String role, Duration expiration) {
        Instant now = Instant.now();
        Instant expiry = now.plus(expiration);

        return Jwts.builder()
            .subject(subject)
            .claim(JwtConfig.AUTHORIZATION_KEY, role)
            .expiration(Date.from(expiry))
            .issuedAt(Date.from(now))
            .signWith(secretKey, SIG.HS512)
            .compact();
    }
}