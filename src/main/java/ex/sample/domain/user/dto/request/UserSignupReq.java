package ex.sample.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserSignupReq(
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    String email,

    @NotBlank(message = "닉네임은 공백이 될 수 없습니다.")
    @Size(min = 2, max = 12, message = "닉네임의 글자수는 2자 이상 12자 이하입니다.")
    String nickname,

    @NotBlank(message = "비밀번호는 공백이 될 수 없습니다.")
    @Size(min = 8, max = 60, message = "비밀번호의 글자수는 8자 이상 60자 이하입니다.")
    String password
) {

}
