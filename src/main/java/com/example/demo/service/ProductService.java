package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) { this.productRepository = productRepository; }

    public List<Product> findAll() { return productRepository.findAll(); }
    public Optional<Product> findById(Long id) { return productRepository.findById(id); }

    // Business rule: product price must be positive and name unique
    @Transactional
    public Product create(Product p) { 
        if (p.getPrice() <= 0) throw new RuntimeException("Product price must be positive");
        if (productRepository.findByName(p.getName()).isPresent()) throw new RuntimeException("Product name must be unique");
        return productRepository.save(p);
    }

    @Transactional
    public Product update(Long id, Product updated) {
        Product p = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        if (updated.getPrice() <= 0) throw new RuntimeException("Product price must be positive");
        p.setName(updated.getName());
        p.setPrice(updated.getPrice());
        p.setStock(updated.getStock());
        return productRepository.save(p);
    }

    @Transactional
    public void delete(Long id) { productRepository.deleteById(id); }
}
