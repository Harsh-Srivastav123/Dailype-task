package com.dailype.dailypetask.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ManagerDto {
    @JsonProperty("manager_id")
    private String managerId;

    @NotEmpty(message = "Manager name must not be empty")
    @JsonProperty("manager_name")
    private String managerName;
}
