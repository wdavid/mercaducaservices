package com.project.mercaduca.controllers;

import com.project.mercaduca.dtos.BusinessRequestDTO;
import com.project.mercaduca.dtos.BusinessUpdateDTO;
import com.project.mercaduca.services.BusinessService;
import com.project.mercaduca.services.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/business")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @PutMapping("/profile")
    public ResponseEntity<String> updateBusinessProfile(
            @RequestParam("priceRange") String priceRange,
            @RequestParam("facebook") String facebook,
            @RequestParam("instagram") String instagram,
            @RequestParam("phone") String phone,
            @RequestPart(value = "logo", required = false) MultipartFile logo
    ) {
        try {
            String logoUrl = null;
            if (logo != null && !logo.isEmpty()) {
                logoUrl = cloudinaryService.uploadImage(logo);
            }

            BusinessUpdateDTO dto = new BusinessUpdateDTO();
            dto.setPriceRange(priceRange);
            dto.setFacebook(facebook);
            dto.setInstagram(instagram);
            dto.setPhone(phone);
            dto.setUrlLogo(logoUrl);

            businessService.updateBusiness(dto);

            return ResponseEntity.ok("Negocio actualizado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar negocio: " + e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<BusinessRequestDTO> getMyBusiness() {
        return ResponseEntity.ok(businessService.getBusinessOfAuthenticatedUser());
    }
}