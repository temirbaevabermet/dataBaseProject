package com.example.phones_repair.service;

import com.example.phones_repair.dto.details.DetailOrderRequest;
import com.example.phones_repair.dto.details.DetailOrderResponse;
import com.example.phones_repair.entities.AvailableDetails;

import java.util.List;

public interface DetailsService {
    void orderDetail(DetailOrderRequest orderRequest);

    List<AvailableDetails> getAllAvailableDetails();

    void deleteOrder(Long id);

    List<DetailOrderResponse> showOrders();

    void sendDetail(Long orderId);

    List<DetailOrderResponse> showInProgressOrders(Long supplierId);

    List<DetailOrderResponse> showFinishedOrders(Long supplierId);

    List<DetailOrderResponse> showSuppliersOrders(Long supplierId);
}
