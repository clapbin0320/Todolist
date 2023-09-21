package web.todolist.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import javax.validation.constraints.NotNull;

@UtilityClass
public class DiaryRequest {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Register {

        @NotNull
        String date;

        @NotNull
        String title;

        @NotNull
        String content;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Info {

        @NotNull
        String date;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Update {

        String date;

        String title;

        String content;
    }
}
