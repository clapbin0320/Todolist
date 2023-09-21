package web.todolist.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import web.todolist.dto.common.BaseResponse;
import web.todolist.dto.common.BaseResponseStatus;
import web.todolist.dto.request.UserRequest;
import web.todolist.dto.response.UserResponse;
import web.todolist.service.EmailService;
import web.todolist.service.UserService;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    /**
     * NAME : 이메일 중복 확인
     * DATE : 2023-09-04
     */
    @PostMapping("/check-email")
    public BaseResponse<?> checkEmail(@Valid @RequestBody UserRequest.Email request) {
        userService.checkEmail(request.getEmail());
        return BaseResponse.success(BaseResponseStatus.OK);
    }

    /**
     * NAME : 이메일 인증 번호 발송
     * DATE : 2023-09-04
     */
    @PostMapping("/verify-email")
    public BaseResponse<?> sendEmail(@Valid @RequestBody UserRequest.Email request) {
        emailService.sendEmail(request);
        return BaseResponse.success(BaseResponseStatus.OK);
    }

    /**
     * NAME : 이메일 인증 번호 검증
     * DATE : 2023-09-04
     */
    @GetMapping("/verify-email")
    public BaseResponse<String> checkNumber(@RequestParam String number) {
        return BaseResponse.success(BaseResponseStatus.OK, emailService.checkNumber(number));
    }

    /**
     * NAME : 회원 가입
     * DATE : 2023-08-30
     */
    @PostMapping("/join")
    public BaseResponse<UserResponse.Join> join(@Valid @RequestBody UserRequest.Join request) {
        return BaseResponse.success(BaseResponseStatus.CREATED, userService.join(request));
    }

    /**
     * NAME : 로그인
     * DATE : 2023-09-14
     */
    @PostMapping("/login")
    public BaseResponse<UserResponse.Login> login(@Valid @RequestBody UserRequest.Login request) {
        return BaseResponse.success(BaseResponseStatus.OK, userService.login(request));
    }

    /**
     * NAME : 로그아웃
     * DATE : 2023-09-15
     */
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public BaseResponse<?> logout() {
        userService.logout();
        return BaseResponse.success(BaseResponseStatus.OK);
    }

    /**
     * NAME : 회원 정보 조회
     * DATE : 2023-09-21
     */
    @GetMapping("/info")
    @PreAuthorize("isAuthenticated()")
    public BaseResponse<UserResponse.Info> getUserInfo() {
        return BaseResponse.success(BaseResponseStatus.OK, userService.getUserInfo());
    }

    /**
     * NAME : 회원 정보 수정
     * DATE : 2023-09-21
     */
    @PatchMapping("/info")
    @PreAuthorize("isAuthenticated()")
    public BaseResponse<?> updateUserInfo(@Valid @RequestBody UserRequest.Update request) {
        userService.updateUserInfo(request);
        return BaseResponse.success(BaseResponseStatus.OK);
    }

    /**
     * NAME : 회원 탈퇴
     * DATE : 2023-09-21
     */
    @DeleteMapping("/info")
    @PreAuthorize("isAuthenticated()")
    public BaseResponse<?> deleteUser() {
        userService.deleteUser();
        return BaseResponse.success(BaseResponseStatus.OK);
    }
}
