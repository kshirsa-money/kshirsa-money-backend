package com.kshirsa.notificationservice;

import com.kshirsa.userservice.UserConstants;
import com.kshirsa.userservice.externalservice.geolite.LocationFromIpResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.kshirsa.notificationservice.EmailTemplates.welcomeHtml;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final SendAsyncEmail sendAsyncEmail;

    public void sendOtpByEmail(EmailValidationType emailType, String email, String otp) throws MessagingException, UnsupportedEncodingException {
        String subject = "Hello from Kshirsa! Your OTP is " + otp;
        sendAsyncEmail.sendEmail(welcomeHtmlTemplate(emailType, otp), subject, email.toLowerCase());
    }

    public void sendWelcomeMail(String email) throws MessagingException, UnsupportedEncodingException {
        String subject = "Welcome to Kshirsa!";
        sendAsyncEmail.sendEmail(welcomeHtml, subject, email.toLowerCase());
    }

    public void sendNewDeviceLoginEmail(String email, LocationFromIpResponse location) throws MessagingException, UnsupportedEncodingException {
        String subject = "New Device Login";
        sendAsyncEmail.sendEmail(newDeviceHtmlTemplate(location), subject, email.toLowerCase());
    }

    public String welcomeHtmlTemplate(EmailValidationType emailType, String otp) {
        return String.format(EmailTemplates.otpHtmlTemplate, emailType.getFirstLine(), otp, UserConstants.OTP_VALIDITY);
    }

    public String newDeviceHtmlTemplate(LocationFromIpResponse location) {
        return String.format(EmailTemplates.newDeviceLogin, location.ipAddress(), location.location(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' h:mm a '(IST)'")));
    }
}
