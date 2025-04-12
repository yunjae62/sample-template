package ex.sample.global.security.authentication;

import java.util.Collection;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@Getter
@ToString(callSuper = true)
public class RefreshTokenAuthentication extends JwtAuthentication {

    /**
     * 인증 전
     */
    public RefreshTokenAuthentication(String token) {
        super(token);
    }

    /**
     * 인증 후
     */
    public RefreshTokenAuthentication(
        Object principal,
        String token,
        Collection<? extends GrantedAuthority> authorities
    ) {
        super(principal, token, authorities);
    }
}