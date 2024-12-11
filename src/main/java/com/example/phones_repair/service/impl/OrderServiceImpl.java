package com.example.phones_repair.service.impl;

import com.example.phones_repair.dto.order.MakeOrderRequest;
import com.example.phones_repair.dto.order.MakeOrderResponse;
import com.example.phones_repair.entities.Client;
import com.example.phones_repair.entities.Order;
import com.example.phones_repair.exception.NotFoundException;
import com.example.phones_repair.mapper.OrderMapper;
import com.example.phones_repair.repositories.ClientRepository;
import com.example.phones_repair.repositories.OrderRepository;
import com.example.phones_repair.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void executeOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The order with id: "+ id +" is not found!", HttpStatus.BAD_REQUEST));
        order.setStatus("READY");
        orderRepository.save(order);
    }

    @Override
    public List<MakeOrderResponse> showOrders() {
        return orderMapper.toDtoS(orderRepository.findAll());
    }

    @Override
    public List<MakeOrderResponse> showFinishedOrders() {
        List<Order> inProgressOrders = orderRepository.findByStatus("READY");

        return orderMapper.toDtoS(inProgressOrders);
    }

    @Override
    public void addOrder(MakeOrderRequest orderRequest) {
        Order order = new Order();
        String orderName = orderRequest.getOrder_option() + " " + orderRequest.getOption_subject();
        Client client = clientRepository.findById(orderRequest.getClientId())
                .orElseThrow(() -> new NotFoundException("Client not found", HttpStatus.BAD_REQUEST));
        order.setClient(client);
        order.setOrder_name(orderName);
        order.setStatus("IN PROGRESS");
        switch (orderName) {
            case "repair display":
                order.setPrice(50);
                break;
            case "repair keyboard":
                order.setPrice(25);
                break;
            case "repair motherboard":
                order.setPrice(40);
                break;
            case "replace battery":
                order.setPrice(15);
                break;
            case "replace display":
                order.setPrice(40);
                break;
            case "replace processor":
                order.setPrice(40);
                break;
            case "replace motherboard":
                order.setPrice(40);
                break;
            case "replace RAM":
                order.setPrice(20);
                break;
            case "clean dust":
                order.setPrice(5);
                break;
            case "clean scratches":
                order.setPrice(10);
                break;
        }

        try {
            orderRepository.save(order);
        } catch (Exception e) {
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAA");
        }

    }

    @Override
    public List<MakeOrderResponse> showInProgressOrders() {
        List<Order> inProgressOrders = orderRepository.findByStatus("IN PROGRESS");

        return orderMapper.toDtoS(inProgressOrders);
    }

    @Override
    public List<MakeOrderResponse> showClientOrders(Long clientId) {
        List<Order> clientOrders = orderRepository.findByClientId(clientId);

        return orderMapper.toDtoS(clientOrders);
    }

    @Override
    public List<MakeOrderResponse> showRepairOrders() {
        List<Order> orders = orderRepository.findByOrderOption("repair");

        return orderMapper.toDtoS(orders);
    }

    @Override
    public List<MakeOrderResponse> showReplaceOrders() {
        List<Order> orders = orderRepository.findByOrderOption("replace");

        return orderMapper.toDtoS(orders);
    }

    @Override
    public List<MakeOrderResponse> showServiceOrders() {
        List<Order> orders = orderRepository.findByOrderOption("clean");

        return orderMapper.toDtoS(orders);
    }

    @Override
    public List<Map<String, Object>> showLargestOrder() {
        return orderRepository.findLargestOrders();
    }

    @Override
    public List<Map<String, Object>> showSmallesttOrder() {
        return orderRepository.findSmallestOrders();
    }
}
