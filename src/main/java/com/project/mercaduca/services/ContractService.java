package com.project.mercaduca.services;

import com.project.mercaduca.dtos.ContractRequestDTO;
import com.project.mercaduca.dtos.PaymentDTO;
import com.project.mercaduca.dtos.PaymentSimpleDTO;
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
import java.util.List;
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

    public Contract createContract(Long userId, Double amount, String paymentMethod, String paymentFrequency) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Optional<Contract> existingContract = contractRepository.findByUserId(userId);
        if (existingContract.isPresent()) {
            throw new IllegalStateException("El usuario ya tiene un contrato");
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(3);
        Contract contract = new Contract();
        contract.setUser(user);
        contract.setStartDate(startDate);
        contract.setEndDate(endDate);
        contract.setStatus("ACTIVO");
        contract.setRenewalRequested(false);
        contract.setPaymentFrequency(paymentFrequency.toUpperCase());

        contractRepository.save(contract);

        Payment pagoInicial = new Payment();
        pagoInicial.setUser(user);
        pagoInicial.setDate(startDate);
        pagoInicial.setExpectedDate(startDate);
        pagoInicial.setAmount(amount);
        pagoInicial.setPaymentMethod(paymentMethod);
        pagoInicial.setStatus("PAGADO");
        pagoInicial.setKindOfPayment(paymentFrequency.toUpperCase());
        paymentRepository.save(pagoInicial);

        if (paymentFrequency.equalsIgnoreCase("MENSUAL")) {
            for (int i = 1; i < 3; i++) {
                LocalDate expected = startDate.plusMonths(i);
                Payment pago = new Payment();
                pago.setUser(user);
                pago.setExpectedDate(expected);
                pago.setAmount(amount);
                pago.setStatus("PENDIENTE");
                pago.setKindOfPayment(paymentFrequency.toUpperCase());
                paymentRepository.save(pago);
            }

            contract.setNextPaymentDate(startDate.plusMonths(1));

        } else if (paymentFrequency.equalsIgnoreCase("TRIMESTRAL")) {
            contract.setNextPaymentDate(null);
        } else {
            throw new IllegalArgumentException("Frecuencia de pago no válida");
        }

        contractRepository.save(contract);

        return contract;
    }

    public void registrarPagoAdmin(Long paymentId, String metodo) {
        Payment pago = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        if ("PAGADO".equalsIgnoreCase(pago.getStatus())) {
            throw new IllegalStateException("El pago ya fue registrado previamente");
        }

        pago.setDate(LocalDate.now());
        pago.setPaymentMethod(metodo);
        pago.setStatus("PAGADO");

        paymentRepository.save(pago);

        User user = pago.getUser();
        Contract contrato = contractRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));

        LocalDate nextPendingDate = paymentRepository.findByUserOrderByExpectedDateAsc(user).stream()
                .filter(p -> "PENDIENTE".equalsIgnoreCase(p.getStatus()))
                .map(Payment::getExpectedDate)
                .findFirst()
                .orElse(null);

        contrato.setNextPaymentDate(nextPendingDate);
        contractRepository.save(contrato);
    }



    public List<PaymentSimpleDTO> getPendingPaymentsForAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByMail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Payment> pagosPendientes = paymentRepository.findByUserOrderByExpectedDateAsc(user);

        return pagosPendientes.stream().map(p -> {
            PaymentSimpleDTO dto = new PaymentSimpleDTO();
            dto.setId(p.getId());
            dto.setPaymentMethod(p.getPaymentMethod());
            dto.setStatus(p.getStatus());
            dto.setExpectedDate(p.getExpectedDate());
            return dto;
        }).toList();
    }


    public List<PaymentDTO> getPaymentsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Payment> pagos = paymentRepository.findByUserOrderByExpectedDateAsc(user);

        return pagos.stream().map(p -> {
            PaymentDTO dto = new PaymentDTO();
            dto.setId(p.getId());
            dto.setDate(p.getDate());
            dto.setExpectedDate(p.getExpectedDate());
            dto.setAmount(p.getAmount());
            dto.setStatus(p.getStatus());
            dto.setPaymentMethod(p.getPaymentMethod());
            dto.setKindOfPayment(p.getKindOfPayment());
            return dto;
        }).toList();
    }



}
