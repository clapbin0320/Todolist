package web.todolist.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import javax.validation.constraints.NotNull;

@UtilityClass
public class TodoRequest {

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Register {

        // todo: 로그인 구현 후 userId 삭제
        @NotNull
        Long userId;

        String category;

        @NotNull
        String date;

        @NotNull
        String todo;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Info {

        // todo: 로그인 구현 후 userId 삭제
        @NotNull
        Long userId;

        @NotNull
        String date;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Update {

        // todo: 로그인 구현 후 userId 삭제
        @NotNull
        Long userId;

        String category;

        String date;

        String todo;
    }
}
