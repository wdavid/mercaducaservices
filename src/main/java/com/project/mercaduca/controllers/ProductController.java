package com.project.mercaduca.controllers;

import com.project.mercaduca.dtos.*;
import com.project.mercaduca.models.Product;
import com.project.mercaduca.models.ProductApproval;
import com.project.mercaduca.models.User;
import com.project.mercaduca.repositories.ProductApprovalRepository;
import com.project.mercaduca.repositories.ProductRepository;
import com.project.mercaduca.repositories.UserRepository;
import com.project.mercaduca.services.CloudinaryService;
import com.project.mercaduca.services.EmailService;
import com.project.mercaduca.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductApprovalRepository productApprovalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping
    @PreAuthorize("hasRole('EMPRENDEDOR')")
    public ResponseEntity<?> createProduct(
            @RequestParam("image") MultipartFile image,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("stock") int stock,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("price") Double price
    ) {
        try {
            String imageUrl = cloudinaryService.uploadImage(image);
            ProductCreateDTO dto = new ProductCreateDTO(name, description, stock, imageUrl, categoryId, price);

            productService.createProduct(dto);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Producto enviado para aprobación.");
            response.put("success", true);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al crear producto: " + e.getMessage());
            response.put("success", false);

            return ResponseEntity.badRequest().body(response);
        }
    }


    @GetMapping("/business/{businessId}/approved")
    public ResponseEntity<?> getBusinessWithApprovedProducts(@PathVariable Long businessId) {
        try {
            return ResponseEntity.ok(productService.getBusinessWithApprovedProducts(businessId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/review")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> reviewProduct(
            @PathVariable Long id,
            @RequestBody ProductReviewDTO reviewDTO
    ) {
        try {
            productService.reviewProduct(id, reviewDTO.isAprobado(), reviewDTO.getRemarks());
            return ResponseEntity.ok("Producto " + (reviewDTO.isAprobado() ? "aprobado" : "rechazado") + " exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al revisar producto: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllApprovedProducts() {
        return ResponseEntity.ok(productService.getApprovedProducts());
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPendingProducts(@RequestParam Long businessId) {
        return ResponseEntity.ok(productService.getPendingProducts(businessId));
    }

    @GetMapping("/business/{businessId}")
    @PreAuthorize("hasRole('EMPRENDEDOR')")
    public ResponseEntity<?> getProductsByBusiness(@PathVariable Long businessId) {
        return ResponseEntity.ok(productService.getProductsByBusiness(businessId));
    }

    @PostMapping("/approve-batch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveProductsBatch(@RequestBody ProductApprovalRequestDTO request) {
        Set<Long> approvedIds = new HashSet<>(request.getApprovedProductIds());
        Set<Long> rejectedIds = new HashSet<>(request.getRejectedProductIds());

        Set<Long> intersection = new HashSet<>(approvedIds);
        intersection.retainAll(rejectedIds);

        if (!intersection.isEmpty()) {
            return ResponseEntity.badRequest().body("Un producto no puede ser aprobado y rechazado al mismo tiempo. IDs conflictivos: " + intersection);
        }

        List<Product> approvedProducts = productRepository.findAllById(approvedIds);
        List<Product> rejectedProducts = productRepository.findAllById(rejectedIds);

        LocalDate now = LocalDate.now();

        for (Product product : approvedProducts) {
            product.setStatus("APROBADO");
            ProductApproval approval = productApprovalRepository.findByProduct(product)
                    .orElse(new ProductApproval());
            approval.setProduct(product);
            approval.setStatus("APROBADO");
            approval.setReviewDate(now);
            approval.setRemarks(request.getRemark());
            productApprovalRepository.save(approval);
        }

        for (Product product : rejectedProducts) {
            product.setStatus("RECHAZADO");
            ProductApproval approval = productApprovalRepository.findByProduct(product)
                    .orElse(new ProductApproval());
            approval.setProduct(product);
            approval.setStatus("RECHAZADO");
            approval.setReviewDate(now);
            approval.setRemarks(request.getRemark());
            productApprovalRepository.save(approval);
        }

        productRepository.saveAll(approvedProducts);
        productRepository.saveAll(rejectedProducts);


        if (!approvedProducts.isEmpty()) {
            User user = approvedProducts.get(0).getBusiness().getOwner();
            emailService.sendProductApprovalSummaryEmail(user, approvedProducts, rejectedProducts, request.getRemark());
        } else if (!rejectedProducts.isEmpty()) {
            User user = rejectedProducts.get(0).getBusiness().getOwner();
            emailService.sendProductApprovalSummaryEmail(user, approvedProducts, rejectedProducts, request.getRemark());
        }

        return ResponseEntity.ok("Productos procesados correctamente.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOwnProduct(@PathVariable Long id) {
        productService.deleteOwnProduct(id);
        return ResponseEntity.ok("Producto marcado como eliminado.");
    }

    @PatchMapping(value = "/update/{id}", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('EMPRENDEDOR')")
    public ResponseEntity<?> updateProductWithImage(
            @PathVariable Long id,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "stock", required = false) Integer stock,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        try {
            String imageUrl = null;

            if (image != null && !image.isEmpty()) {
                imageUrl = cloudinaryService.uploadImage(image);
            }

            ProductUpdateRequestDTO dto = new ProductUpdateRequestDTO();
            dto.setDescription(description);
            dto.setStock(stock);
            dto.setUrlImage(imageUrl);

            productService.updateProductFields(id, dto);
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar producto: " + e.getMessage());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterProducts(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        return ResponseEntity.ok(productService.filterProducts(status, categoryName, minPrice, maxPrice));
    }

    @GetMapping("/my-products")
    @PreAuthorize("hasRole('EMPRENDEDOR')")
    public ResponseEntity<?> getMyProducts(
            @RequestParam(required = false) String status
    ) {
        return ResponseEntity.ok(productService.getProductsByAuthenticatedUserAndStatus(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            ProductResponseDTO product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
