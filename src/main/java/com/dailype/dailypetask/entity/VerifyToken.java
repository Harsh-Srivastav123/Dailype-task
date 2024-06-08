package com.dailype.dailypetask.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class VerifyToken {

    public  static final int EXPIRATION_TIME=5;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer tokenId;
    String token;
    Date expirationTime;

    @OneToOne
    @JoinColumn(name = "userId",nullable = false)
    UserSecured userSecured;



    public VerifyToken(String token, UserSecured userSecured) {
        super();
        this.token = token;
        this.userSecured = userSecured;
        this.expirationTime=calculateExpirationTime(EXPIRATION_TIME);
    }

    private Date calculateExpirationTime(int expirationTime) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE,expirationTime);
        return new Date(calendar.getTime().getTime());
    }
}
