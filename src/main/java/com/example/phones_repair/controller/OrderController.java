package com.example.phones_repair.controller;

import com.example.phones_repair.dto.order.MakeOrderRequest;
import com.example.phones_repair.dto.order.MakeOrderResponse;
import com.example.phones_repair.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/order")
    public void makeOrder(@RequestBody MakeOrderRequest makeOrderRequest) {
        orderService.addOrder(makeOrderRequest);
    }

    @PostMapping("/execute/{id}")
    public void executeOrder(@PathVariable Long id) {
        orderService.executeOrder(id);
    }

    @GetMapping("/category/all")
    public List<MakeOrderResponse> showOrders() {
        return orderService.showOrders();
    }

    @GetMapping("/category/finished")
    public List<MakeOrderResponse> getFinishedOrders() {
        return orderService.showFinishedOrders();
    }

    @GetMapping("/category/in-progress")
    public List<MakeOrderResponse> showInProgressOrders() {
        return orderService.showInProgressOrders();
    }

    @GetMapping("/client/{clientId}")
    public List<MakeOrderResponse> showClientOrders(@PathVariable Long clientId) {
        return orderService.showClientOrders(clientId);
    }

    @GetMapping("/category/repair")
    public List<MakeOrderResponse> showRepairOrders() {
        return orderService.showRepairOrders();
    }

    @GetMapping("/category/replace")
    public List<MakeOrderResponse> showReplaceOrders() {
        return orderService.showReplaceOrders();
    }

    @GetMapping("/category/serve")
    public List<MakeOrderResponse> showServiceOrders() {
        return orderService.showServiceOrders();
    }

    @GetMapping("/category/largest")
    public List<Map<String, Object>> showLargestOrder() {
        return orderService.showLargestOrder();
    }

    @GetMapping("/category/smallest")
    public List<Map<String, Object>> showSmallestOrder() {
        return orderService.showSmallesttOrder();
    }
}
