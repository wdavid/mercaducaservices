package com.project.mercaduca.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "inventory_movement")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // E = Entry, S = Exit
    private Integer quantity;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}