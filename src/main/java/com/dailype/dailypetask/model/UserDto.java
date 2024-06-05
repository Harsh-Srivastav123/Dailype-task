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
    @JsonProperty("user_id")
    private String userId;

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

    @JsonProperty("is_active")
    private boolean isActive;

    @NotEmpty(message = "Manager_id must not be empty")
    @JsonProperty("manager_id")
    private String managerId;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("updated_at")
    private Date updatedAt;
}
