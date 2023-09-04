package web.todolist.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import web.todolist.dto.common.ErrorResponse;

@RequiredArgsConstructor
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> customExceptionHandler(CustomException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getError().getCode(), e.getError().getMessage()), e.getError().getHttpStatus());
    }
}
