package com.example.phones_repair.mapper;

import com.example.phones_repair.dto.order.MakeOrderResponse;
import com.example.phones_repair.entities.Order;

import java.util.List;

public interface OrderMapper {
    List<MakeOrderResponse> toDtoS(List<Order> all);
    MakeOrderResponse toDto(Order order);
}
