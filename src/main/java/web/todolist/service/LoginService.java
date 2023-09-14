package web.todolist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import web.todolist.domain.User;
import web.todolist.dto.request.UserRequest;
import web.todolist.exception.CustomException;
import web.todolist.exception.Error;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // todo: 뭔지 모르겠음 애드원 loginService에 있음
    private User login(UserRequest.Login request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return (User) authentication.getPrincipal();
    }

    public Long getLoginUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (UsernamePasswordAuthenticationToken.class.isAssignableFrom(
                Optional.ofNullable(auth).orElseThrow(() -> new CustomException(Error.FORBIDDEN)).getClass())) {
            return Long.valueOf(auth.getName());
        }
        return 0L;
    }
}
