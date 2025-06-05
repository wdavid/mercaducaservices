package com.project.mercaduca.repositories;

import com.project.mercaduca.models.Category;
import com.project.mercaduca.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAll();
}
