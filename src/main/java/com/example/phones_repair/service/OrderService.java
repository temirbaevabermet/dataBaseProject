package com.example.phones_repair.service;

import com.example.phones_repair.dto.order.MakeOrderRequest;
import com.example.phones_repair.dto.order.MakeOrderResponse;

import java.util.List;
import java.util.Map;

public interface OrderService {

    void executeOrder(Long id);

    List<MakeOrderResponse> showOrders();

    List<MakeOrderResponse> showFinishedOrders();

    void addOrder(MakeOrderRequest makeOrderRequest);

    List<MakeOrderResponse> showInProgressOrders();

    List<MakeOrderResponse> showClientOrders(Long clientId);

    List<MakeOrderResponse> showRepairOrders();

    List<MakeOrderResponse> showReplaceOrders();

    List<MakeOrderResponse> showServiceOrders();

    List<Map<String, Object>> showLargestOrder();

    List<Map<String, Object>> showSmallesttOrder();
}
