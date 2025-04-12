package ex.sample.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import ex.sample.global.exception.GlobalException;
import ex.sample.global.response.CommonRes;
import ex.sample.global.response.ResponseCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class ExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (GlobalException e) {
            setErrorResponse(response, e.getResponseCode());
        }
    }

    private void setErrorResponse(HttpServletResponse response, ResponseCode responseCode) {

        response.setStatus(responseCode.getHttpStatus().value()); // HttpStatus 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // Content-Type : application/json
        response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // charset : UTF8

        try {
            String responseJson = objectMapper.writeValueAsString(CommonRes.error(responseCode));
            response.getWriter().write(responseJson);
        } catch (IOException e) {
            log.error("예외 필터 직렬화 오류", e);
        }
    }
}
