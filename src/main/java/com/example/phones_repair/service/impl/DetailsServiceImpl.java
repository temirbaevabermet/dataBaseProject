package com.example.phones_repair.service.impl;

import com.example.phones_repair.dto.details.DetailOrderRequest;
import com.example.phones_repair.dto.details.DetailOrderResponse;
import com.example.phones_repair.entities.AvailableDetails;
import com.example.phones_repair.entities.Details;
import com.example.phones_repair.exception.NotFoundException;
import com.example.phones_repair.mapper.DetailsMapper;
import com.example.phones_repair.repositories.AvailableDetailsRepository;
import com.example.phones_repair.service.DetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class DetailsServiceImpl implements DetailsService {
    @Autowired
    private DetailsRepository detailsRepository;

    @Autowired
    private AvailableDetailsRepository availableDetailsRepository;

    @Autowired
    private DetailsMapper detailsMapper;

    @Override
    public void orderDetail(DetailOrderRequest orderRequest) {
        Details details = new Details();
        AvailableDetails availableDetails = availableDetailsRepository.findById(orderRequest.getDetailId())
                .orElseThrow(() -> new NotFoundException("Detail with ID " + orderRequest.getDetailId() + " not found",
                        HttpStatus.BAD_REQUEST
                ));
        details.setAvailableDetails(availableDetails);
        details.setQuantity(orderRequest.getQuantity());
        details.setStatus("ORDERED");
        details.setOrderDate(LocalDate.now());
        details.setDeliveryDate(details.getOrderDate().plusDays(2));
        detailsRepository.save(details);
    }

    @Override
    public List<AvailableDetails> getAllAvailableDetails() {
        return availableDetailsRepository.findAll();
    }

    @Override
    public void deleteOrder(Long id) {
        if (detailsRepository.findById(id).isEmpty())
            throw new NotFoundException("The detail with id: "+id+" is not found!", HttpStatus.BAD_REQUEST);
        detailsRepository.deleteById(id);
    }

    @Override
    public List<DetailOrderResponse> showOrders() {
        return detailsMapper.toDtoS(detailsRepository.findAll());
    }

    @Override
    public void sendDetail(Long orderId) {
        Details details = detailsRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("The order with id: "+ orderId +" is not found!", HttpStatus.BAD_REQUEST));
        details.setStatus("SEND");
        detailsRepository.save(details);
    }

    @Override
    public List<DetailOrderResponse> showInProgressOrders(Long supplierId) {
        List<Details> inProgressOrders = detailsRepository.findByStatus(supplierId,"ORDERED");

        return detailsMapper.toDtoS(inProgressOrders);
    }

    @Override
    public List<DetailOrderResponse> showFinishedOrders(Long supplierId) {
        List<Details> inProgressOrders = detailsRepository.findByStatus(supplierId,"SEND");

        return detailsMapper.toDtoS(inProgressOrders);
    }

    @Override
    public List<DetailOrderResponse> showSuppliersOrders(Long supplierId) {
        List<Details> suppliersOrders = detailsRepository.findDetailsBySupplierId(supplierId);

        return detailsMapper.toDtoS(suppliersOrders);
    }
}
