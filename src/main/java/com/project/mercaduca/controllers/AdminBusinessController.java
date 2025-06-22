package com.project.mercaduca.controllers;

import com.project.mercaduca.dtos.BusinessApprovedDTO;
import com.project.mercaduca.dtos.BusinessStatusUpdateDTO;
import com.project.mercaduca.dtos.ContractRequestDTO;
import com.project.mercaduca.services.AdminBusinessService;
import com.project.mercaduca.services.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<BusinessApprovedDTO>> getApprovedBusinesses() {
        List<BusinessApprovedDTO> approvedList = adminBusinessService.getBusinessApproved();
        return ResponseEntity.ok(approvedList);
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

        adminBusinessService.updateBusinessStatus(businessId, statusDTO.getStatus());
        return ResponseEntity.noContent().build();
    }
}
