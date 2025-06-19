package com.project.mercaduca.services;

import com.project.mercaduca.dtos.ContractRequestDTO;
import com.project.mercaduca.models.Contract;
import com.project.mercaduca.models.Payment;
import com.project.mercaduca.models.User;
import com.project.mercaduca.repositories.ContractRepository;
import com.project.mercaduca.repositories.PaymentRepository;
import com.project.mercaduca.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ContractService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public ContractRequestDTO getContractDataForAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByMail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Contract contract = contractRepository.findByUser(user).orElse(null);
        Payment payment = paymentRepository.findTopByUserOrderByDateDesc(user).orElse(null);

        ContractRequestDTO dto = new ContractRequestDTO();

        if (user.getBusiness() != null) {
            dto.setBusinessName(user.getBusiness().getBusinessName());
            dto.setPhone(user.getBusiness().getPhone());

            User owner = user.getBusiness().getOwner();
            if (owner != null) {
                dto.setOwnerName(owner.getName());
                dto.setOwnerLastName(owner.getLastName());
                dto.setOwnerMail(owner.getMail());
            }

        }

        if (contract != null) {
            dto.setStartDate(contract.getStartDate());
            dto.setEndDate(contract.getEndDate());
            dto.setStatus(contract.getStatus());
            dto.setNextPaymentDate(contract.getNextPaymentDate());
        }

        if (payment != null) {
            dto.setPaymentMethod(payment.getPaymentMethod());
            dto.setKindOfPayment(payment.getKindOfPayment());
        }

        return dto;
    }

    public Contract createContract(Long userId, Double amount, String kindOfPayment, String paymentMethod, String paymentFrequency) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Optional<Contract> existingContract = contractRepository.findByUserId(userId);
        if (existingContract.isPresent()) {
            throw new IllegalStateException("El usuario ya tiene un contrato");
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(6);

        LocalDate nextPaymentDate;

        switch (paymentFrequency.toUpperCase()) {
            case "MENSUAL":
                nextPaymentDate = startDate.plusMonths(1);
                break;
            case "TRIMESTRAL":
                nextPaymentDate = startDate.plusMonths(3);
                break;
            case "SEMESTRAL":
                nextPaymentDate = startDate.plusMonths(6);
                break;
            default:
                throw new IllegalArgumentException("Frecuencia de pago no válida");
        }

        Contract contract = new Contract();
        contract.setUser(user);
        contract.setStartDate(startDate);
        contract.setEndDate(endDate);
        contract.setStatus("ACTIVO");
        contract.setRenewalRequested(false);
        contract.setNextPaymentDate(nextPaymentDate);
        contract.setPaymentFrequency(paymentFrequency.toUpperCase());

        contractRepository.save(contract);

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setDate(startDate);
        payment.setAmount(amount);
        payment.setKindOfPayment(kindOfPayment);
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus("PAGADO");

        paymentRepository.save(payment);

        return contract;
    }

    public void registrarPago(Long userId, Double amount, String metodo, String tipo) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Contract contrato = contractRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));

        Payment pago = new Payment();
        pago.setUser(user);
        pago.setDate(LocalDate.now());
        pago.setAmount(amount);
        pago.setPaymentMethod(metodo);
        pago.setKindOfPayment(tipo);
        pago.setStatus("PAGADO");

        paymentRepository.save(pago);

        LocalDate nuevaFecha;
        switch (contrato.getPaymentFrequency()) {
            case "MENSUAL":
                nuevaFecha = contrato.getNextPaymentDate().plusMonths(1);
                break;
            case "TRIMESTRAL":
                nuevaFecha = contrato.getNextPaymentDate().plusMonths(3);
                break;
            default:
                throw new IllegalStateException("Frecuencia de pago desconocida");
        }

        contrato.setNextPaymentDate(nuevaFecha);
        contractRepository.save(contrato);
    }


}
