package com.project.mercaduca.repositories;

import com.project.mercaduca.models.Product;
import com.project.mercaduca.models.ProductApproval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductApprovalRepository extends JpaRepository<ProductApproval, Long> {
    Optional<ProductApproval> findByProduct(Product product);
}
