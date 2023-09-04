package web.todolist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.todolist.domain.User;
import web.todolist.dto.request.UserRequest;
import web.todolist.exception.CustomException;
import web.todolist.exception.Error;
import web.todolist.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 이메일 중복 확인
     */
    @Transactional(readOnly = true)
    public void checkEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new CustomException(Error.ALREADY_SAVED_EMAIL);
        });
    }

    /**
     * 회원가입
     */
    @Transactional
    public void join(UserRequest.Join request) {
        // 이메일 중복 확인
        checkEmail(request.getEmail());

        // 비밀번호 길이 확인
        if (request.getPassword().length() < 8) {
            throw new CustomException(Error.BAD_PASSWORD);
        }

        // 비밀번호 암호화
        String encPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(encPassword)
                .build();
        userRepository.save(user);
    }
}
