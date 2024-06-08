package com.dailype.dailypetask.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RequestSecuredPayload {

    @JsonProperty("user_secured_id")
    private String userSecuredId;

    @JsonProperty("mob_num")
    private String mobNum;

    @JsonProperty("manager_id")
    private String managerId;

}
