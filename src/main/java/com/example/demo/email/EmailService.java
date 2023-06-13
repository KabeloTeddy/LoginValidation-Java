package com.example.demo.email;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender{

    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Override
    @Async
    public void send(String Sendto, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper sender =
                    new MimeMessageHelper(mimeMessage, "utf-8");//used to create the message body and set the recipient, subject, and sender of the email.
            sender.setText(email, true);
            sender.setTo(Sendto);//sets recipient email address
            sender.setSubject("Hey!Teddy Here, Please confirm your email");//auto set subject
            sender.setFrom("morriskabeloteddy51@gmail.com"); //sets the sender email address
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error("We’re sorry, but there was an error sending your email. Please check your email address and try again", e);
            throw new IllegalStateException("failed to send email");
        }
    }
}
