package com.example.phones_repair.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "available_details")
public class AvailableDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(
            name = "supplier_id",
            referencedColumnName = "id",
            nullable = true
    )
    @JsonBackReference
    private Supplier supplier;

    private Integer price;
}
