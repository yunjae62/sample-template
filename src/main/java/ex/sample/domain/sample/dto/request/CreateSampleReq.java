package ex.sample.domain.sample.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

public record CreateSampleReq(

    @Pattern(regexp = "^[a-zA-Z가-힣0-9]{1,20}$", message = "이름은 한글, 영문, 숫자만 20자 이내로 입력 가능합니다.")
    String name,

    @NotNull(message = "금액은 필수값입니다.")
    BigDecimal money
) {

}
