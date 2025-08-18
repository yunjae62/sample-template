package ex.sample.infra.mail;

import ex.sample.global.exception.GlobalException;
import ex.sample.global.response.ResponseCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.MimeMessage;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationMailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void send(String to, String subject, String verificationCode) {
        try {
            String html = generateHtml(verificationCode);
            MimeMessage mimeMessage = generateMail(to, subject, html);
            mailSender.send(mimeMessage);
            log.info("메일 정상 발송 : {}", to);
        } catch (MailException e) {
            log.error("메일 전송 에러 (수신자 : {})  : {}", to, e.getMessage(), e);
            throw new GlobalException(ResponseCode.MAIL_SEND_FAIL);
        }
    }

    private MimeMessage generateMail(String to, String subject, String html) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            return mimeMessage;
        } catch (AddressException e) {
            log.error("잘못된 메일 주소 에러 (수신자 : {}) : {}", to, e.getMessage(), e);
            throw new GlobalException(ResponseCode.BAD_MAIL_ADDRESS);
        } catch (MessagingException e) {
            log.error("메일 전송 에러 : {}", e.getMessage(), e);
            throw new GlobalException(ResponseCode.MAIL_SEND_FAIL);
        }
    }

    private String generateHtml(String verificationCode) {
        Context context = new Context(Locale.KOREA);
        context.setVariable("verificationCode", verificationCode);
        return templateEngine.process("mail-verification", context);
    }
}
