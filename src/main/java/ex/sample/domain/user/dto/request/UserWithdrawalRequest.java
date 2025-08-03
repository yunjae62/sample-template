package ex.sample.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserWithdrawalRequest(
    @NotBlank(message = "비밀번호는 공백이 될 수 없습니다.")
    @Size(min = 8, max = 60, message = "비밀번호의 글자수는 8자 이상 60자 이하입니다.")
    String password
) {

}
