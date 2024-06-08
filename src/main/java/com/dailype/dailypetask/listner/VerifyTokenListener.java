package com.dailype.dailypetask.listner;

import com.dailype.dailypetask.event.VerifyTokenEvent;
import com.dailype.dailypetask.model.UserSecuredDto;
import com.dailype.dailypetask.services.MailService;
import com.dailype.dailypetask.services.UserSecuredService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class VerifyTokenListener implements ApplicationListener<VerifyTokenEvent> {

    @Autowired
    UserSecuredService userSecuredService;

    @Autowired
    MailService mailService;
    @Override
    public void onApplicationEvent(VerifyTokenEvent event) {

        UserSecuredDto user=event.getUserSecuredDto();
        String token= UUID.randomUUID().toString();
        userSecuredService.saveVerificationToken(user,token);
        String url=event.getUrl().toString()+"/secured/verifyRegistration?token="+token;
        log.info("click here to verify account   "+ url);
        mailService.sendMail(user.getEmail(),url);
    }
}
