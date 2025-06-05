package com.project.mercaduca.components;

import com.project.mercaduca.models.Category;
import com.project.mercaduca.repositories.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryDataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public CategoryDataLoader(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            List<Category> initialCategories = List.of(
                    new Category("Ropa", "Prendas de vestir y moda"),
                    new Category("Comida", "Alimentos preparados o empacados"),
                    new Category("Accesorios", "Bolsos, bisutería, gafas, relojes"),
                    new Category("Tecnología", "Productos electrónicos y gadgets"),
                    new Category("Artesanías", "Productos hechos a mano")
            );
            categoryRepository.saveAll(initialCategories);
            System.out.println("✅ Categorías iniciales cargadas correctamente.");
        }
    }
}