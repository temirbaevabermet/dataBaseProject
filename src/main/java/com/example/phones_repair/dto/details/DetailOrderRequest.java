package com.example.phones_repair.dto.details;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DetailOrderRequest {
    private Long detailId;
    private Integer quantity;
}
