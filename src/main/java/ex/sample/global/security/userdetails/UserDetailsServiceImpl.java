package ex.sample.global.security.userdetails;

import ex.sample.domain.user.domain.User;
import ex.sample.domain.user.repository.UserRepository;
import ex.sample.global.exception.GlobalException;
import ex.sample.global.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new GlobalException(ResponseCode.USER_NOT_FOUND));

        return new UserDetailsImpl(user);
    }
}
