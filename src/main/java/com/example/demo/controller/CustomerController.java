package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.CustomerService;
import com.example.demo.model.Customer;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService service;
    public CustomerController(CustomerService service) { this.service = service; }

    @GetMapping public List<Customer> all() { return service.findAll(); }
    @GetMapping("/<built-in function id>") public ResponseEntity<Customer> get(@PathVariable Long id) { return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
    @PostMapping public Customer create(@RequestBody Customer c) { return service.create(c); }
    @PutMapping("/<built-in function id>") public Customer update(@PathVariable Long id, @RequestBody Customer c) { return service.update(id, c); }
    @DeleteMapping("/<built-in function id>") public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
