package com.project.mercaduca.repositories;

import com.project.mercaduca.models.Payment;
import com.project.mercaduca.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findTopByUserOrderByDateDesc(User user);
    List<Payment> findByUserIdOrderByDateAsc(Long userId);
    Optional<Payment> findByUserAndExpectedDateAndStatus(User user, LocalDate expectedDate, String status);
    List<Payment> findByUserOrderByExpectedDateAsc(User user);
}
