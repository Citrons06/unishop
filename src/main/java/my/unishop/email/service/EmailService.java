package my.unishop.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final HttpSession httpSession;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendMail(String to, String verificationCode) {
        if (!isEmail(to)) {
            throw new IllegalArgumentException("유효하지 않은 이메일입니다.");
        }

        httpSession.setAttribute("emailVerificationCode", verificationCode);

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject("회원가입 인증번호");
            String body = "";
            body += "<h3>" + "인증 번호" + "</h3>";
            body += "<h1>" + verificationCode + "</h1>";
            message.setText(body,"UTF-8", "html");

            javaMailSender.send(message);
        } catch (MessagingException e) {
            log.error("이메일 전송 실패", e);
            throw new RuntimeException("이메일 전송에 실패했습니다.");
        }
    }

    private boolean isEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public String generateVerificationCode() {
        return String.valueOf((int) (Math.random() * 9000) + 1000);
    }
}