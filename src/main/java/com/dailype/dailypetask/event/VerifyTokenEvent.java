package com.dailype.dailypetask.event;

import com.dailype.dailypetask.model.UserSecuredDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
@Getter
public class VerifyTokenEvent extends ApplicationEvent {

    UserSecuredDto userSecuredDto;
    String url;
    public VerifyTokenEvent( UserSecuredDto userSecuredDto , String url) {
        super(userSecuredDto);
        this.userSecuredDto=userSecuredDto;
        this.url=url;
    }
}
