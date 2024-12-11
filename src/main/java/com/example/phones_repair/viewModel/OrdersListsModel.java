package com.example.phones_repair.viewModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdersListsModel {
    private int id;
    private String clientId;
    private String name;
    private String status;
    private int price;

    public OrdersListsModel(int id, String clientId, String name, String status, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.clientId = clientId;
        this.status = status;
    }

    public String toString() {
        return  "name: " + name
                + "\nclient: " + clientId
                + "\nstatus: " + status
                + "\nprice: " + price;
    }
}
