package com.example.phones_repair.mapper.impl;

import com.example.phones_repair.dto.details.DetailOrderResponse;
import com.example.phones_repair.entities.Details;
import com.example.phones_repair.mapper.DetailsMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DetailsMapperImpl implements DetailsMapper {
    @Override
    public List<DetailOrderResponse> toDtoS(List<Details> all) {
        List<DetailOrderResponse> orderResponses = new ArrayList<>();
        for (Details details: all){
            orderResponses.add(toDto(details));
        }
        return orderResponses;
    }

    @Override
    public DetailOrderResponse toDto(Details details) {
        DetailOrderResponse orderResponse = new DetailOrderResponse();
        orderResponse.setId(details.getId());
        orderResponse.setDetailId(details.getAvailableDetails().getId());
        orderResponse.setStatus(details.getStatus());
        orderResponse.setQuantity(details.getQuantity());
        orderResponse.setOrderDate(details.getOrderDate());
        orderResponse.setDeliveryDate(details.getDeliveryDate());
        return orderResponse;
    }
}
