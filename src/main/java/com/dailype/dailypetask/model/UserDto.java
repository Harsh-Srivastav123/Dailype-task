package com.dailype.dailypetask.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String user_id;
    @NotEmpty(message = "Full name must not be empty")
    private String full_name;

    @NotEmpty(message = "Mobile number must not be empty")
    @Pattern(regexp = "^(\\+91|0)?[6789]\\d{9}$", message = "Mobile number must be a valid 10-digit number")
    private String mob_num;

    @NotEmpty(message = "PAN number must not be empty")
    @Pattern(regexp = "(?i)[a-z]{5}[0-9]{4}[a-z]{1}", message = "PAN number must be a valid PAN number")
    private String pan_num;

    private boolean is_active;

    @NotEmpty(message = "Manager_id must not be empty")
    private String manager_id;
    private Date created_at;
    private Date updated_at;


}
