package com.example.phones_repair.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MakeOrderResponse {
    private Long id;

    private Long clientId;

    private String orderName;

    private Integer price;

    private String status;
}
