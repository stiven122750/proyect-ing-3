package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.ProductService;
import com.example.demo.model.Product;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;
    public ProductController(ProductService service) { this.service = service; }

    @GetMapping public List<Product> all() { return service.findAll(); }
    @GetMapping("/<built-in function id>") public ResponseEntity<Product> get(@PathVariable Long id) { return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
    @PostMapping public Product create(@RequestBody Product p) { return service.create(p); }
    @PutMapping("/<built-in function id>") public Product update(@PathVariable Long id, @RequestBody Product p) { return service.update(id, p); }
    @DeleteMapping("/<built-in function id>") public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
