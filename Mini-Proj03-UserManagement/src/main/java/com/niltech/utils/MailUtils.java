package com.niltech.utils;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class MailUtils {

    private final JavaMailSender sender;
    
    private static final Logger logger = LoggerFactory.getLogger(MailUtils.class);


    // Constructor injection
    public MailUtils(JavaMailSender sender) {
        this.sender = sender;
    }

    public boolean sendMail(String to, String subject, String body) {
        boolean status = false;
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setSentDate(new Date());
            sender.send(message);
            status = true;
        } catch (MessagingException e) {
        	logger.error("Failed to send email to {}: {}", to, e.getMessage());        }
        return status;
    }
}
