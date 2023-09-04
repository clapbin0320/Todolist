package web.todolist.dto.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum BaseResponseStatus {

    OK(HttpStatus.OK, "요청에 성공하였습니다."),
    CREATED(HttpStatus.CREATED, "요청에 따른 리소스 생성에 성공하였습니다.");

    final HttpStatus httpStatus;
    final String message;
}
