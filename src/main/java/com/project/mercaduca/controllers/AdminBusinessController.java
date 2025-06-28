package com.project.mercaduca.controllers;

import com.project.mercaduca.dtos.BusinessApprovedDTO;
import com.project.mercaduca.dtos.BusinessStatusUpdateDTO;
import com.project.mercaduca.dtos.ContractRequestDTO;
import com.project.mercaduca.services.AdminBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/business")
public class AdminBusinessController {
    @Autowired
    private AdminBusinessService adminBusinessService;


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAnyBusiness(@PathVariable Long id) {
        adminBusinessService.deleteAnyBusiness(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/approved")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<BusinessApprovedDTO>> getFilteredBusinesses(
            @RequestParam(required = false) List<String> statuses,
            @RequestParam(required = false) Boolean tienenContrato,
            @RequestParam(required = false) Boolean tienenProductosPendientes,
            Pageable pageable
    ) {
        Page<BusinessApprovedDTO> result = adminBusinessService.getFilteredBusinesses(statuses, tienenContrato, tienenProductosPendientes, pageable);
        return ResponseEntity.ok(result);
    }



    @GetMapping("/contracts/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContractRequestDTO> getContractByUserId(@PathVariable Long userId) {
        ContractRequestDTO dto = adminBusinessService.getContractById(userId);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/businesses/disable/{businessId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateBusinessStatus(
            @PathVariable Long businessId,
            @RequestBody BusinessStatusUpdateDTO statusDTO) {

        adminBusinessService.updateBusinessStatusByOwnerId(businessId, statusDTO.getStatus());
        return ResponseEntity.noContent().build();
    }
}
