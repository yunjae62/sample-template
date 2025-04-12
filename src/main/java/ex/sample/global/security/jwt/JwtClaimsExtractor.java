package ex.sample.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtClaimsExtractor {

    private final JwtParser jwtParser;

    public Claims getClaims(String token) {
        return jwtParser.parseSignedClaims(token).getPayload();
    }

    public String getSubject(String token) {
        return this.getClaims(token).getSubject();
    }

    public String getRole(String token) {
        return (String) this.getClaims(token).get(JwtConfig.AUTHORIZATION_KEY);
    }
}
