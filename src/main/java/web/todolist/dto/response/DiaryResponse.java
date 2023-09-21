package web.todolist.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DiaryResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Register {

        Long diaryId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Info {

        Long diaryId;

        String title;

        String content;
    }
}
