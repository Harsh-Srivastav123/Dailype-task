package com.dailype.dailypetask.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserSecuredDto {
    @JsonProperty("user_id")
    private String userSecuredId;

    @NotEmpty(message = "Full name must not be empty")
    @JsonProperty("full_name")
    private String fullName;

    @NotEmpty(message = "Mobile number must not be empty")
    @Pattern(regexp = "^(\\+91|0)?[6789]\\d{9}$", message = "Mobile number must be a valid 10-digit number")
    @JsonProperty("mob_num")
    private String mobNum;

    @NotEmpty(message = "PAN number must not be empty")
    @Pattern(regexp = "(?i)[a-z]{5}[0-9]{4}[a-z]{1}", message = "PAN number must be a valid PAN number")
    @JsonProperty("pan_num")
    private String panNum;

    @JsonProperty(value = "is_active",access = JsonProperty.Access.READ_ONLY)
    private boolean isActive;

    @JsonProperty("manager_id")
    private String managerId;

    @JsonProperty(value = "created_at",access = JsonProperty.Access.READ_ONLY)
    private Date createdAt;

    @JsonProperty(value = "updated_at",access = JsonProperty.Access.READ_ONLY)
    private Date updatedAt;

    //extended

    @JsonProperty(value = "is_verified",access = JsonProperty.Access.READ_ONLY)
    private boolean isVerified;

    @JsonProperty(value = "user_name")
    @NotEmpty(message = "UserName must not be empty")
    private String userName;

    @JsonProperty(value = "password",access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(message = "Password must not be empty")

    private String password;

    @Email(message = "Email should be valid")
    @JsonProperty("email")
    private String email;

    @JsonProperty(value = "image_url",access = JsonProperty.Access.READ_ONLY)
    private String imageUrl;
}
