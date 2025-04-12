package ex.sample.global.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CommonResponse<T>(
    @Schema(description = "응답 코드", example = "0")
    String code, // 커스텀 응답 코드

    @Schema(description = "응답 메시지", example = "정상 처리 되었습니다.")
    String message, // 응답에 대한 설명

    @Schema(description = "응답 데이터", example = "{}")
    T data // 응답에 필요한 데이터
) {

    /**
     * data 필드에 값을 넣을 때 사용하는 메서드 - data 필드가 필요 없는 경우
     */
    public static CommonResponse<CommonEmptyRes> success() {
        return new CommonResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), new CommonEmptyRes());
    }

    /**
     * data 필드에 값을 넣을 때 사용하는 메서드 - data 필드가 필요한 경우
     */
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }

    /**
     * 에러 발생 시 특정 에러에 맞는 응답하는 메서드 - data 필드가 필요 없는 경우
     */
    public static CommonResponse<CommonEmptyRes> error(ResponseCode responseCode) {
        return new CommonResponse<>(responseCode.getCode(), responseCode.getMessage(), new CommonEmptyRes());
    }

    /**
     * 에러 발생 시 특정 에러에 맞는 응답하는 메서드 - data 필드가 필요한 경우
     */
    public static <T> CommonResponse<T> error(ResponseCode responseCode, T data) {
        return new CommonResponse<>(responseCode.getCode(), responseCode.getMessage(), data);
    }
}
