package ex.sample.domain.user.service;

import ex.sample.domain.user.dto.request.UserSignupReq;
import ex.sample.domain.user.dto.request.UserWithdrawalReq;
import ex.sample.domain.user.model.User;
import ex.sample.domain.user.repository.UserRepository;
import ex.sample.global.exception.GlobalException;
import ex.sample.global.response.CommonEmptyRes;
import ex.sample.global.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CommonEmptyRes signup(UserSignupReq request) {
        // 닉네임이 존재하는지 검증
        validateNickname(request.nickname());

        // 이메일이 존재하는지 검증
        validateEmail(request.email());

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.of(request.nickname(), request.email(), encodedPassword);

        userRepository.save(user);

        return new CommonEmptyRes();
    }

    private void validateNickname(String nickname) {
        boolean existsByNickname = userRepository.existsByNickname(nickname);

        if (existsByNickname) {
            throw new GlobalException(ResponseCode.DUPLICATED_NICKNAME);
        }
    }

    private void validateEmail(String email) {
        boolean existsByEmail = userRepository.existsByEmail(email);

        if (existsByEmail) {
            throw new GlobalException(ResponseCode.DUPLICATED_EMAIL);
        }
    }

    @Transactional
    public CommonEmptyRes deleteUser(UserDetails userDetails, UserWithdrawalReq request) {
        String email = userDetails.getUsername();

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new GlobalException(ResponseCode.BAD_REQUEST));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new GlobalException(ResponseCode.BAD_REQUEST);
        }

        userRepository.deleteByEmail(userDetails.getUsername());

        return new CommonEmptyRes();
    }
}
