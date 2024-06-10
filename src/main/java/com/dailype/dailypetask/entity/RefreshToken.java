package com.dailype.dailypetask.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer refreshId;
    private String token;

    private Instant expiryDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn( name = "userSecuredId")
    private UserSecured userSecured;


}
