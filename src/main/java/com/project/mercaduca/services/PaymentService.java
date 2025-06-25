package com.project.mercaduca.services;

import com.project.mercaduca.models.Payment;
import com.project.mercaduca.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public List<Payment> getAllPaymentsForUser(Long userId) {
        return paymentRepository.findByUserIdOrderByDateAsc(userId);
    }

}
