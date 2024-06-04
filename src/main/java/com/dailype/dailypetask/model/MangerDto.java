package com.dailype.dailypetask.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MangerDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String manager_id;
    @NotEmpty(message = "Manager name must not be empty")
    private String manager_name;
}
