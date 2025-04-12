package ex.sample.global.exception;

import ex.sample.global.response.CommonEmptyRes;
import ex.sample.global.response.CommonResponse;
import ex.sample.global.response.ResponseCode;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 전역 예외 처리 핸들러
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final InvalidInputMapper mapper;
    private final HttpServletResponse response; // HttpStatus 설정을 위한 response 객체

    /**
     * Business 오류 발생에 대한 핸들러
     */
    @ExceptionHandler(GlobalException.class)
    public CommonResponse<CommonEmptyRes> handleGlobalException(GlobalException e) {
        response.setStatus(e.getResponseCode().getHttpStatus().value()); // HttpStatus 설정

        return CommonResponse.error(e.getResponseCode()); // 공통 응답 양식 반환
    }

    /**
     * RequestBody 입력 파라미터가 없거나 형식이 맞지 않을 때 발생하는 오류에 대한 핸들러
     */
    @ExceptionHandler({
        HttpMessageNotReadableException.class,
        MethodArgumentTypeMismatchException.class,
        HttpRequestMethodNotSupportedException.class,
        HttpMediaTypeNotAcceptableException.class,
        HttpMediaTypeNotSupportedException.class,
        MissingPathVariableException.class,
        MissingServletRequestParameterException.class
    })
    public CommonResponse<CommonEmptyRes> handleGlobalException(Exception e) {
        response.setStatus(ResponseCode.INVALID_INPUT.getHttpStatus().value()); // HttpStatus 설정

        return CommonResponse.error(ResponseCode.INVALID_INPUT); // 공통 응답 양식 반환
    }

    /**
     * Validation 라이브러리로 RequestBody 입력 파라미터 검증 오류 발생에 대한 핸들러
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<List<InvalidInputRes>> handlerValidationException(MethodArgumentNotValidException e) {
        response.setStatus(ResponseCode.INVALID_INPUT.getHttpStatus().value()); // HttpStatus 설정

        // 잘못된 입력 에러들을 DTO 변환
        List<InvalidInputRes> invalidInputResList = changeFieldErrorToDto(e);

        return CommonResponse.error(ResponseCode.INVALID_INPUT, invalidInputResList); // 공통 응답 양식 반환
    }

    private List<InvalidInputRes> changeFieldErrorToDto(MethodArgumentNotValidException e) {
        return e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(mapper::toInvalidInputResponseDto) // defaultMessage 필드명을 message 변경
            .toList();
    }

    /**
     * 예상치 못한 에러 발생에 대한 핸들러
     */
    @ExceptionHandler({Exception.class, RuntimeException.class})
    public CommonResponse<CommonEmptyRes> handleException(Exception e) {
        log.error("예상치 못한 에러 발생", e);
        response.setStatus(ResponseCode.SYSTEM_ERROR.getHttpStatus().value()); // HttpStatus 설정

        return CommonResponse.error(ResponseCode.SYSTEM_ERROR); // 공통 응답 양식 반환
    }
}
