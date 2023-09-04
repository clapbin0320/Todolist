package web.todolist.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Error {

    // 400 BAD_REQUEST 잘못된 요청
    BAD_REQUEST(400, "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    BAD_PASSWORD(4001, "비밀번호는 8자 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    BAD_AUTH_NUMBER(4002, "잘못된 인증번호입니다.", HttpStatus.BAD_REQUEST),

    // 401 UNAUTHORIZED 권한없음(인증 실패)

    // 403 FORBIDDEN 권한없음(인가 실패)
    INVALID_PASSWORD(4003, "올바르지 않은 비밀번호입니다.", HttpStatus.FORBIDDEN),

    // 404 NOT_FOUND 잘못된 리소스 접근
    NOT_FOUND_USER(4041, "존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),

    // 409 CONFLICT 중복된 리소스
    ALREADY_SAVED_EMAIL(4091, "이미 가입된 이메일입니다.", HttpStatus.CONFLICT),

    // 500 INTERNAL_SERVER_ERROR 서버 내부 에러
    INTERNAL_SERVER_ERROR(500, "서버 내부 에러입니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
