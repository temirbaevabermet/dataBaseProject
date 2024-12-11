package com.example.phones_repair.dto.details;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DetailOrderResponse {
    private Long id;

    private Long detailId;

    private Integer quantity;

    private String status;

    private LocalDate orderDate;

    private LocalDate deliveryDate;
}
