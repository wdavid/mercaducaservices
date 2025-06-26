package com.project.mercaduca.controllers;

import com.project.mercaduca.dtos.*;
import com.project.mercaduca.enums.PaymentFrequency;
import com.project.mercaduca.enums.PaymentMethod;
import com.project.mercaduca.models.Contract;
import com.project.mercaduca.models.Payment;
import com.project.mercaduca.services.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                request.getPaymentMethod(),
                request.getPaymentFrequency()
        );

        return ResponseEntity.ok("Contrato creado exitosamente con ID: " + newContract.getId());
    }

    @PostMapping("/payment-by-id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registrarPagoPorId(
            @RequestParam Long paymentId,
            @RequestParam String paymentMethod) {
        try {
            contractService.registrarPagoAdmin(paymentId, paymentMethod);
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

    @GetMapping("/my-payments")
    public ResponseEntity<List<PaymentSimpleDTO>> getMyUpcomingPayments() {
        List<PaymentSimpleDTO> pagos = contractService.getPendingPaymentsForAuthenticatedUser();
        return ResponseEntity.ok(pagos);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user-payments/{userId}")
    public ResponseEntity<List<PaymentDTO>> getUserPayments(@PathVariable Long userId) {
        List<PaymentDTO> pagos = contractService.getPaymentsByUserId(userId);
        return ResponseEntity.ok(pagos);
    }

    @PostMapping("/renew/{userId}")
    public ResponseEntity<?> renovarContrato(
            @PathVariable Long userId,
            @RequestParam Double amount,
            @RequestParam String paymentMethod) {

        Contract contratoRenovado = contractService.renovarContrato(userId, amount, paymentMethod);
        return ResponseEntity.ok(contractService.mapToDTO(contratoRenovado));
    }

}
