package com.dailype.dailypetask.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailService {

    @Value("$(DailyPe-App)")
    String from;
    @Autowired
    JavaMailSender javaMailSender;

    public void sendMail(String mail, String message) {
        log.info("mail send to =  " + mail);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setSubject("Verify Email to login to DailyPe app");
        simpleMailMessage.setText("verify your email  " + message);
        simpleMailMessage.setTo(mail);
        javaMailSender.send(simpleMailMessage);
    }
}
