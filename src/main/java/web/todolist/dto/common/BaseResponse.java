package web.todolist.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonPropertyOrder({"status", "message", "data"}) // Json 으로 나갈 순서를 설정하는 어노테이션
@JsonInclude(JsonInclude.Include.NON_NULL) // Json으로 응답이 나갈 때 - null인 필드는(CLASS LEVEL에 붙었으니) 응답으로 포함시키지 않는 어노테이션
public class BaseResponse<T> {

    @JsonProperty("status")
    HttpStatus httpStatus;

    @JsonProperty("message")
    String message;

    T data;

    private BaseResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    private BaseResponse(HttpStatus httpStatus, String message, T data) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.data = data;
    }

    public static BaseResponse<?> success(BaseResponseStatus status) {
        return new BaseResponse<>(status.getHttpStatus(), status.getMessage());
    }

    public static <T> BaseResponse<T> success(BaseResponseStatus status, T data) {
        return new BaseResponse<T>(status.getHttpStatus(), status.getMessage(), data);
    }
}
