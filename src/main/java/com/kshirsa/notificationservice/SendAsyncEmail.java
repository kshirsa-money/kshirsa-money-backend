package com.kshirsa.notificationservice;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class SendAsyncEmail {

    @Value("spring.mail.username")
    String fromEmail;

    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(String content, String subject, String toEmail) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        mimeMessage.setContent(content, "text/html");
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setFrom(fromEmail,"Kshirsa Money");
        mailSender.send(mimeMessage);
    }
}
