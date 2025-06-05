package com.project.mercaduca.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "contract")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Boolean renewalRequested = false;
    private LocalDate nextPaymentDate;
    private String paymentFrequency;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;
}