package ex.sample.global.security.provider;

import ex.sample.global.exception.GlobalException;
import ex.sample.global.redis.RedisUtil;
import ex.sample.global.response.ResponseCode;
import ex.sample.global.security.authentication.AccessTokenAuthentication;
import ex.sample.global.security.jwt.JwtBearerUtils;
import ex.sample.global.security.jwt.JwtClaimsExtractor;
import ex.sample.global.security.jwt.JwtStatus;
import ex.sample.global.security.jwt.JwtValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessTokenAuthenticationProvider implements AuthenticationProvider {

    private final RedisUtil redisUtil;
    private final JwtValidator jwtValidator;
    private final JwtClaimsExtractor jwtClaimsExtractor;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = getTokenWithoutBearer(authentication);

        validateTokenStatus(token);

        String subject = jwtClaimsExtractor.getSubject(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(subject);

        // 로그아웃 여부 확인 후 예외처리
        validateLogout(subject);

        return new AccessTokenAuthentication(userDetails, token, userDetails.getAuthorities());
    }

    private String getTokenWithoutBearer(Authentication authentication) {
        String tokenWithBearer = (String) authentication.getCredentials();
        return JwtBearerUtils.removePrefix(tokenWithBearer);
    }

    private void validateTokenStatus(String token) {
        JwtStatus tokenStatus = jwtValidator.validate(token);

        if (tokenStatus.equals(JwtStatus.INVALID)) {
            throw new GlobalException(ResponseCode.INVALID_TOKEN);
        }

        if (tokenStatus.equals(JwtStatus.EXPIRED)) {
            throw new GlobalException(ResponseCode.EXPIRED_ACCESS_TOKEN);
        }
    }

    private void validateLogout(String subject) {
        redisUtil.get(subject, String.class)
            .orElseThrow(() -> new GlobalException(ResponseCode.LOGIN_REQUIRED));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AccessTokenAuthentication.class.isAssignableFrom(authentication);
    }
}