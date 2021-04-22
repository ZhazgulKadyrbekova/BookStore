package kg.PerfectJob.BookStore.service;

import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;

    private final Environment environment;

    public MailService(JavaMailSender javaMailSender, Environment environment) {
        this.javaMailSender = javaMailSender;
        this.environment = environment;
    }

    public boolean send(String toEmail, String subject, String text) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(text);
            simpleMailMessage.setTo(toEmail);
            simpleMailMessage.setFrom(Objects.requireNonNull(environment.getProperty("spring.mail.username")));
            javaMailSender.send(simpleMailMessage);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
