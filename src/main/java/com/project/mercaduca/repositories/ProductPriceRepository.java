package com.project.mercaduca.repositories;

import com.project.mercaduca.models.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    Optional<ProductPrice> findTopByProductIdAndEndDateIsNullOrderByStartDateDesc(Long productId);

}
