package ex.sample.global.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CommonResponse<T>(
    @Schema(description = "응답 코드", example = "0")
    Integer code, // 커스텀 응답 코드

    @Schema(description = "응답 메시지", example = "정상 처리 되었습니다.")
    String message, // 응답에 대한 설명

    @Schema(description = "응답 데이터", example = "{}")
    T data // 응답에 필요한 데이터
) {

    /**
     * data 필드에 값을 넣을 때 사용하는 메서드 - data 필드가 필요 없는 경우
     */
    public static CommonResponse<CommonEmptyRes> success() {
        return new CommonResponse<>(0, "정상 처리 되었습니다.", new CommonEmptyRes());
    }

    /**
     * data 필드에 값을 넣을 때 사용하는 메서드 - data 필드가 필요한 경우
     */
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(0, "정상 처리 되었습니다.", data);
    }

    /**
     * 에러 발생 시 특정 에러에 맞는 응답하는 메서드 - data 필드가 필요 없는 경우
     */
    public static CommonResponse<CommonEmptyRes> error(ErrorCase errorCase) {
        return new CommonResponse<>(errorCase.getCode(), errorCase.getMessage(), new CommonEmptyRes());
    }

    /**
     * 에러 발생 시 특정 에러에 맞는 응답하는 메서드 - data 필드가 필요한 경우
     */
    public static <T> CommonResponse<T> error(ErrorCase errorCase, T data) {
        return new CommonResponse<>(errorCase.getCode(), errorCase.getMessage(), data);
    }
}
