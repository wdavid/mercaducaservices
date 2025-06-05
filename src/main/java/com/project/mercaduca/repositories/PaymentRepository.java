package com.project.mercaduca.repositories;

import com.project.mercaduca.models.Payment;
import com.project.mercaduca.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findTopByUserOrderByDateDesc(User user);
}
