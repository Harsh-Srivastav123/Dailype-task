package com.dailype.dailypetask.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class UserSecured {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_secured_id")
    private String userSecuredId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "mob_num", nullable = false)
    private String mobNum;

    @Column(name = "pan_num", nullable = false)
    private String panNum;

    @Column(name = "manager_id")
    private String managerId;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    // Extended fields



    @Column(name = "is_verified", nullable = false)
    private boolean isVerified;

    @Column(name = "email", nullable = false)
    private String email;


    @Column(name = "username", nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "image_url")
    private String imageUrl;
}
