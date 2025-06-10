package com.project.mercaduca.repositories;

import com.project.mercaduca.models.Business;
import com.project.mercaduca.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStatus(String status);
    List<Product> findByBusinessAndStatus(Business business, String status);
    List<Product> findByBusiness(Business business);
    List<Product> findByStatusInAndBusinessId(List<String> statuses, Long businessId);
    long countByBusiness(Business business);
    List<Product> findByBusinessAndStatusNot(Business business, String status);
    long countByBusinessAndStatusNot(Business business, String status);

}
