package web.todolist.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@UtilityClass
public class UserRequest {

    // todo: @Email 해결

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Email {

        @NotNull
//        @Email(message = "이메일 형식이어야 합니다.")
        String email;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Join {

        @NotNull
        String nickname;

        @NotNull
//        @Email(message = "이메일 형식이어야 합니다.")
        String email;

        @NotNull
        String password;
    }
}
