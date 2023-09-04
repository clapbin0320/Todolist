package web.todolist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import web.todolist.dto.request.UserRequest;
import web.todolist.exception.CustomException;
import web.todolist.exception.Error;
import web.todolist.util.RedisUtil;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final UserService userService;
    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    /**
     * 이메일 인증번호 발송
     */
    public void sendEmail(UserRequest.Email request) {
        userService.checkEmail(request.getEmail());
        String key = createKey();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(request.getEmail());
            mimeMessageHelper.setSubject("투두리스트 이메일 인증번호");
            mimeMessageHelper.setText("투두리스트 이메일 인증번호 : " + key, true);
            javaMailSender.send(mimeMessage);

            redisUtil.setDateExpire(key, request.getEmail(), 300000L); // 제한시간 5분

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 인증번호 생성
     */
    private String createKey() {
        StringBuilder key = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) {
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }

    /**
     * 인증번호 검증
     */
    public String checkNumber(String number) {
        String data = redisUtil.getData(number);
        if (data == null) {
            throw new CustomException(Error.BAD_AUTH_NUMBER);
        }
        redisUtil.deleteData(number);
        return "올바른 인증번호입니다.";
    }
}
