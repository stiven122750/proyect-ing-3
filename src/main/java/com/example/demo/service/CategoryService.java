package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) { this.repository = repository; }

    public List<Category> findAll() { return repository.findAll(); }
    public Optional<Category> findById(Long id) { return repository.findById(id); }
    @Transactional public Category create(Category c) { return repository.save(c); }
    @Transactional public Category update(Long id, Category updated) { 
        Category c = repository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        c.setName(updated.getName());
        return repository.save(c);
    }
    @Transactional public void delete(Long id) { repository.deleteById(id); }
}
