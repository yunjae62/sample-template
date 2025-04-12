package ex.sample.global.security.jwt;

import ex.sample.global.jwt.JwtStatus;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtValidator {

    private final JwtParser jwtParser;

    public JwtStatus validate(String token) {
        try {
            jwtParser.parseSignedClaims(token);
            return JwtStatus.VALID;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
            return JwtStatus.EXPIRED;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (JwtException e) {
            log.error("Invalid JWT, 유효하지 않는 JWT 서명 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return JwtStatus.INVALID;
    }
}