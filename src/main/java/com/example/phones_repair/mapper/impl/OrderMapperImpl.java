package com.example.phones_repair.mapper.impl;

import com.example.phones_repair.dto.order.MakeOrderResponse;
import com.example.phones_repair.entities.Order;
import com.example.phones_repair.mapper.OrderMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public List<MakeOrderResponse> toDtoS(List<Order> all) {
        List<MakeOrderResponse> orderResponses = new ArrayList<>();
        for (Order order: all){
            orderResponses.add(toDto(order));
        }
        return orderResponses;
    }

    @Override
    public MakeOrderResponse toDto(Order order) {
        MakeOrderResponse orderResponse = new MakeOrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setClientId(order.getClient().getId());
        orderResponse.setOrderName(order.getOrder_name());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setPrice(order.getPrice());
        return orderResponse;
    }
}
