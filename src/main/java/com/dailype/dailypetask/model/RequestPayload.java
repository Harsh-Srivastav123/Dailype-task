package com.dailype.dailypetask.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RequestPayload {
    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("mob_num")
    private String mobNum;

    @JsonProperty("manager_id")
    private String managerId;
}
