package com.example.phones_repair.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "client_id",
            referencedColumnName = "id",
            nullable = true
    )
    @JsonBackReference
    private Client client;

    private String order_name;

    private String status;

    private Integer price;

}
