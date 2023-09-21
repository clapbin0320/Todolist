package web.todolist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.todolist.domain.User;
import web.todolist.dto.request.UserRequest;
import web.todolist.dto.response.UserResponse;
import web.todolist.exception.CustomException;
import web.todolist.exception.Error;
import web.todolist.repository.UserRepository;
import web.todolist.security.JwtProvider;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final LoginService loginService;

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

    /**
     * 로그인
     */
    @Transactional
    public UserResponse.Login login(UserRequest.Login request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_EMAIL));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(Error.INVALID_PASSWORD);
        }

        UserResponse.Token tokenResponse = jwtProvider.generateTokenResponse(user);

        user.changeJwt(tokenResponse.getRefreshToken());
        userRepository.save(user);

        return UserResponse.Login.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .tokenInfo(tokenResponse)
                .build();
    }

    /**
     * 로그아웃
     */
    @Transactional
    public void logout() {
        Long userId = loginService.getLoginUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));
        user.changeJwt(null);
        userRepository.save(user);
    }

    /**
     * 회원 정보 조회
     */
    @Transactional(readOnly = true)
    public UserResponse.Info getUserInfo() {
        Long userId = loginService.getLoginUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        return UserResponse.Info.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }

    /**
     * 회원 정보 수정
     */
    @Transactional
    public void updateUserInfo(UserRequest.Update request) {
        Long userId = loginService.getLoginUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        if (request.getNickname() != null) {
            user.changeNickname(request.getNickname());
        }

        if (request.getEmail() != null) {
            user.changeEmail(request.getEmail());
        }

        userRepository.save(user);
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteUser() {
        Long userId = loginService.getLoginUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER));

        userRepository.delete(user);
    }
}
