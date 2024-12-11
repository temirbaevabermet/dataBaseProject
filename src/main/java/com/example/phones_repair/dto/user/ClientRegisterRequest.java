package com.example.phones_repair.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientRegisterRequest {
    @JsonProperty("email")
    private String email;

    @JsonProperty("fullName")
    private String fullName;

    @JsonProperty("phoneNumber")
    private String phoneNumber;
}
