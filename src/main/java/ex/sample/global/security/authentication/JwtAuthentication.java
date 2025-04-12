package ex.sample.global.security.authentication;

import java.util.Collection;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Getter
@ToString(callSuper = true)
public abstract class JwtAuthentication extends AbstractAuthenticationToken {

    protected final Object principal;
    protected final String token; // jwt token

    /**
     * 인증 전
     */
    protected JwtAuthentication(String token) {
        super(null);
        this.token = token;
        this.principal = null;
        setAuthenticated(false);
    }

    /**
     * 인증 후
     */
    protected JwtAuthentication(
        Object principal,
        String token,
        Collection<? extends GrantedAuthority> authorities
    ) {
        super(authorities);
        this.principal = principal;
        this.token = token;
        setAuthenticated(true);
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }
}