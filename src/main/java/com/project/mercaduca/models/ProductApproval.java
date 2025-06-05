package com.project.mercaduca.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "product_approval")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductApproval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private LocalDate reviewDate;
    private String remarks;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
}