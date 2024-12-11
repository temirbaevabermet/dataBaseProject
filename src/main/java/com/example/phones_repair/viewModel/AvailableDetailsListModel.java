package com.example.phones_repair.viewModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvailableDetailsListModel {
    private int id;
    private String detailName;
    private int price;

    public AvailableDetailsListModel(int id, String detailName, int price) {
        this.id = id;
        this.detailName = detailName;
        this.price = price;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                "\nDetail ID: " + detailName +
                "\nprice: " + price;
    }
}
