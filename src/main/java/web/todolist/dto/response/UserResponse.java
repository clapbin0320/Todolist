package web.todolist.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Login {

        Long userId;

        String nickname;

        String email;

        UserResponse.Token tokenInfo;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Token {

        String tokenType;

        String accessToken;

        Long expiresIn; // 토큰 만료까지 남은 시간

        String refreshToken;

        Long refreshTokenExpiresIn; // refreshToken 만료까지 남은 시간
    }
}
