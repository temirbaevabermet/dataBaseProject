package com.example.phones_repair.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MakeOrderRequest {
    private Long clientId;

    private String order_option;

    private String option_subject;
}
