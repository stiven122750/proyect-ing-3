package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.OrderService;
import com.example.demo.model.OrderEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;
    public OrderController(OrderService service) { this.service = service; }

    @GetMapping public List<OrderEntity> all() { return service.findAll(); }
    @GetMapping("/<built-in function id>") public ResponseEntity<OrderEntity> get(@PathVariable Long id) { return ResponseEntity.ok(service.findById(id)); }

    // create expects JSON: {"customerId":1,"productId":2,"quantity":3}
    @PostMapping public OrderEntity create(@RequestBody Map<String, Object> body) {
        Long customerId = Long.valueOf(String.valueOf(body.get("customerId")));
        Long productId = Long.valueOf(String.valueOf(body.get("productId")));
        int qty = Integer.parseInt(String.valueOf(body.get("quantity")));
        return service.create(customerId, productId, qty);
    }

    @DeleteMapping("/<built-in function id>") public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
