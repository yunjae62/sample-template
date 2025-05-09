package ex.sample.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {

    // 성공 0번대
    SUCCESS(HttpStatus.OK, "0", "정상 처리 되었습니다"),

    /* 글로벌 1000번대 */

    // 권한 없음 403
    NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "1000", "해당 요청에 대한 권한이 없습니다."),
    // 잘못된 형식의 입력 400
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "1001", "유효하지 않은 입력값입니다."),
    // 존재하지 않는 값 404
    NOT_FOUND(HttpStatus.NOT_FOUND, "1002", "존재하지 않는 입력값입니다."),
    // 시스템 에러 500
    SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "1003", "알 수 없는 에러가 발생했습니다."),
    // 잘못된 요청 400
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "1004", "잘못된 요청입니다."),

    /* 유저 2000번대 */

    // 중복된 이메일 입력 409
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "2000", "중복된 이메일을 입력하셨습니다."),
    // 중복된 닉네임 입력 409
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "2001", "중복된 닉네임을 입력하셨습니다."),
    // 존재하지 않는 사용자 404
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "2002", "유저를 찾을 수 없습니다."),
    // 유효하지 않은 토큰 401
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "2003", "유효하지 않은 토큰입니다."),
    // 로그인 필요 401
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "2004", "로그인이 필요합니다."),
    // 만료된 액세스 토큰 401
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "2005", "만료된 Access Token"),
    // 만료된 리프레쉬 토큰 401
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "2006", "만료된 Refresh Token"),
    ;

    private final HttpStatus httpStatus; // 응답 상태 코드
    private final String code; // 응답 코드. 도메인에 따라 1000번대로 나뉨
    private final String message; // 응답에 대한 설명
}
