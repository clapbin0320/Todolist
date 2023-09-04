package web.todolist.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import web.todolist.dto.common.BaseResponse;
import web.todolist.dto.common.BaseResponseStatus;
import web.todolist.dto.request.UserRequest;
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
     * NAME : 이메일 인증번호 발송
     * DATE : 2023-09-04
     */
    @PostMapping("/verify-email")
    public BaseResponse<?> sendEmail(@Valid @RequestBody UserRequest.Email request) {
        emailService.sendEmail(request);
        return BaseResponse.success(BaseResponseStatus.OK);
    }

    /**
     * NAME : 이메일 인증번호 검증
     * DATE : 2023-09-04
     */
    @GetMapping("/verify-email")
    public BaseResponse<String> checkNumber(@RequestParam String number) {
        return BaseResponse.success(BaseResponseStatus.OK, emailService.checkNumber(number));
    }

    /**
     * NAME : 회원가입
     * DATE : 2023-08-30
     */
    @PostMapping("/join")
    public BaseResponse<?> join(@Valid @RequestBody UserRequest.Join request) {
        userService.join(request);
        return BaseResponse.success(BaseResponseStatus.CREATED);
    }

    /**
     * NAME : 로그인
     * DATE :
     */

    /**
     * NAME : 로그아웃
     * DATE :
     */

    /**
     * NAME : 회원정보 수정
     * DATE :
     */

    /**
     * NAME : 회원탈퇴
     * DATE :
     */
}
