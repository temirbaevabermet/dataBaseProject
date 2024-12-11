package com.example.phones_repair.controller;

import com.example.phones_repair.dto.details.DetailOrderRequest;
import com.example.phones_repair.dto.details.DetailOrderResponse;
import com.example.phones_repair.entities.AvailableDetails;

import com.example.phones_repair.service.DetailsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/details")
@AllArgsConstructor
public class DetailsController {
    private DetailsService detailsService;

    @PostMapping("/orderDetail")
    public void orderDetail(@RequestBody DetailOrderRequest orderRequest) {
        detailsService.orderDetail(orderRequest);
    }

    @DeleteMapping("/deleteOrder/{id}")
    public void deleteOrder(@PathVariable Long id) {
        detailsService.deleteOrder(id);
    }

    @GetMapping("/category/all")
    public List<DetailOrderResponse> showOrders() {
        return detailsService.showOrders();
    }

    @GetMapping("/showAvailable")
    public List<AvailableDetails> showAvailableDetails() {
        return detailsService.getAllAvailableDetails();
    }

    @GetMapping("/supplier/{supplierId}/category/all")
    public List<DetailOrderResponse> showSupplierOrders(@PathVariable Long supplierId) {
        return detailsService.showSuppliersOrders(supplierId);
    }

    @PostMapping("/sendDetail/{id}")
    public void sendDetail(@PathVariable Long id) {
        detailsService.sendDetail(id);
    }

    @GetMapping("/supplier/{id}/in-progress")
    public List<DetailOrderResponse> showInProgresDetails(@PathVariable Long id) {
        return detailsService.showInProgressOrders(id);
    }

    @GetMapping("/supplier/{supplierId}/category/send")
    public List<DetailOrderResponse> showFinishedDetails(@PathVariable Long supplierId) {
        return detailsService.showFinishedOrders(supplierId);
    }
}
