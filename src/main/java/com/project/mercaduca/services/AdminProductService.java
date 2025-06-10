package com.project.mercaduca.services;

import com.project.mercaduca.models.Product;
import com.project.mercaduca.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void deleteAnyProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        product.setStatus("ELIMINADO");
        productRepository.save(product);
    }
}