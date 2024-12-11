package com.example.phones_repair.viewModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailOrdersListModel {
    private int id;
    private String detailName;
    private int quantity;
    private String status;
    private String orderDate;
    private String deliveryDate;

    public DetailOrdersListModel(int id, String detailName, int quantity, String status, String orderDate, String deliveryDate) {
        this.id = id;
        this.detailName = detailName;
        this.quantity = quantity;
        this.status = status;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                "\nDetail ID: " + detailName +
                "\nQuantity: " + quantity +
                "\nStatus: " + status +
                "\nOrder Date: " + orderDate +
                "\nDelivery Date: " + deliveryDate;
    }
}
