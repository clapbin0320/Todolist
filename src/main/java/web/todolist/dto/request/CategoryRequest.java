package web.todolist.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import javax.validation.constraints.NotNull;

@UtilityClass
public class CategoryRequest {

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Register {

        // todo: 로그인 구현 후 userId 삭제
        @NotNull
        Long userId;

        @NotNull
        String categoryName;

        @NotNull
        String categoryColor;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Update {

        // todo: 로그인 구현 후 userId 삭제
        @NotNull
        Long userId;

        String categoryName;

        String categoryColor;
    }
}
