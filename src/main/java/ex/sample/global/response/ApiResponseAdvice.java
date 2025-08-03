package ex.sample.global.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 공통응답으로 감싸주는 어드바이스
 */
@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 실패 응답 (GlobalExceptionHandler error 처리가 된 응답)은 이미 ApiResponse 감싸져서 해당되지 않음
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !returnType.getParameterType().equals(ApiResponse.class);
    }

    @Override
    public Object beforeBodyWrite(
        Object body,
        MethodParameter returnType,
        MediaType selectedContentType,
        Class<? extends HttpMessageConverter<?>> selectedConverterType,
        ServerHttpRequest request,
        ServerHttpResponse response
    ) {
        // 빈 응답은 기본 응답으로 처리
        if (body == null || returnType.getParameterType().equals(Void.TYPE)) {
            return ApiResponse.success();
        }

        return ApiResponse.success(body); // 성공 응답에 공통 응답 처리
    }
}
