package com.project.mercaduca.repositories;

import com.project.mercaduca.models.StockProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockProductRepository extends JpaRepository<StockProduct, Long> {

}
