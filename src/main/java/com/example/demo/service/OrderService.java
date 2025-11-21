package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.model.OrderEntity;
import com.example.demo.model.Product;
import com.example.demo.model.Customer;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.CustomerRepository;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    public List<OrderEntity> findAll() {
        return orderRepository.findAll();
    }

    public OrderEntity findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // Business rule: When creating an order, product must have enough stock and customer cannot have more than 5 orders
    @Transactional
    public OrderEntity create(Long customerId, Long productId, int quantity) {
        if (quantity <= 0) throw new RuntimeException("Quantity must be positive");
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        if (product.getStock() < quantity) throw new RuntimeException("Insufficient product stock");
        long count = orderRepository.countByCustomer(customer);
        if (count >= 5)
            throw new RuntimeException("Customer has reached the maximum number of orders (5)"); // second business rule example
        // reduce stock and create order
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
        OrderEntity order = new OrderEntity(customer, product, quantity);
        return orderRepository.save(order);
    }

    @Transactional
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}
