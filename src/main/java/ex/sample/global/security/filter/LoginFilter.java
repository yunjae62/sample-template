package ex.sample.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import ex.sample.global.exception.GlobalException;
import ex.sample.global.response.ResponseCode;
import ex.sample.global.security.WebSecurityConfig;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final AuthenticationSuccessHandler loginSuccessHandler;

    @PostConstruct
    public void setup() {
        this.setAuthenticationSuccessHandler(loginSuccessHandler);
        setFilterProcessesUrl(WebSecurityConfig.LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // 요청 JSON 파싱
            UserLoginReq req = objectMapper.readValue(request.getInputStream(), UserLoginReq.class);

            // 인증 처리 로직
            Authentication preAuthentication = new UsernamePasswordAuthenticationToken(req.email(), req.password(), null);
            return getAuthenticationManager().authenticate(preAuthentication);
        } catch (IOException e) {
            throw new GlobalException(ResponseCode.SYSTEM_ERROR);
        }
    }

    /**
     * 로그인 실패 시 처리 로직
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.info("login failed message : {}", failed.getMessage());
        throw new GlobalException(ResponseCode.NOT_FOUND);
    }

    public record UserLoginReq(
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,

        @NotBlank(message = "비밀번호는 공백이 될 수 없습니다.")
        @Size(min = 8, max = 60, message = "비밀번호의 글자수는 8자 이상 60자 이하입니다.")
        String password
    ) {

    }
}