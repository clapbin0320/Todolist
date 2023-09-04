package web.todolist.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final Error error;

    public CustomException(Error error) {
        this.error = error;
    }
}
