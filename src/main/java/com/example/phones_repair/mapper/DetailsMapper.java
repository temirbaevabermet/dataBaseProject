package com.example.phones_repair.mapper;

import com.example.phones_repair.dto.details.DetailOrderResponse;
import com.example.phones_repair.entities.Details;

import java.util.List;

public interface DetailsMapper {
    List<DetailOrderResponse> toDtoS(List<Details> all);
    DetailOrderResponse toDto(Details details);
}
