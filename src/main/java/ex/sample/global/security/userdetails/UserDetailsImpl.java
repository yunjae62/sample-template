package ex.sample.global.security.userdetails;

import ex.sample.domain.user.model.User;
import ex.sample.domain.user.model.UserRole;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record UserDetailsImpl(User user) implements UserDetails {

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRole role = user.getRole();
        return List.of(role::getAuthority);
    }
}