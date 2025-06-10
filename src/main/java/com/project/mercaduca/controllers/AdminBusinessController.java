package com.project.mercaduca.controllers;

import com.project.mercaduca.services.AdminBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
