package com.dailype.dailypetask.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String user_id;
    private String full_name;
    private String mob_num;
    private String pan_num;
    private String manager_id;
    private boolean is_active;
    private Date created_at;
    private Date updated_at;

}
