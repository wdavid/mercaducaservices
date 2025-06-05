package com.project.mercaduca.controllers;

import com.project.mercaduca.dtos.ContractCreateDTO;
import com.project.mercaduca.dtos.ContractRequestDTO;
import com.project.mercaduca.dtos.PaymentCreateDTO;
import com.project.mercaduca.enums.PaymentFrequency;
import com.project.mercaduca.enums.PaymentMethod;
import com.project.mercaduca.models.Contract;
import com.project.mercaduca.services.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contract")
public class ContractController {
    @Autowired
    private ContractService contractService;

    @GetMapping("/me")
    public ResponseEntity<ContractRequestDTO> getMyContractData() {
        ContractRequestDTO dto = contractService.getContractDataForAuthenticatedUser();
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createContract(@RequestBody ContractCreateDTO request) {
        Contract newContract = contractService.createContract(
                request.getUserId(),
                request.getAmount(),
                request.getKindOfPayment(),
                request.getPaymentMethod(),
                request.getPaymentFrequency()
        );

        return ResponseEntity.ok("Contrato creado exitosamente con ID: " + newContract.getId());
    }

    @PostMapping("/payment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registrarPago(@RequestBody PaymentCreateDTO request) {
        try {
            contractService.registrarPago(
                    request.getUserId(),
                    request.getAmount(),
                    request.getPaymentMethod(),
                    request.getKindOfPayment()
            );
            return ResponseEntity.ok("Pago registrado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/payment-methods")
    public ResponseEntity<PaymentMethod[]> getPaymentMethods() {
        return ResponseEntity.ok(PaymentMethod.values());
    }

    @GetMapping("/payment-frequencies")
    public ResponseEntity<PaymentFrequency[]> getPaymentFrequencies() {
        return ResponseEntity.ok(PaymentFrequency.values());
    }



}
