package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public CustomerService(CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer create(Customer c) {
        return customerRepository.save(c);
    }

    @Transactional
    public Customer update(Long id, Customer updated) {
        Customer c = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        c.setName(updated.getName());
        c.setEmail(updated.getEmail());
        return customerRepository.save(c);
    }

    // Business rule: don't allow deleting customer if they have orders
    @Transactional
    public void delete(Long id) {
        Customer c = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        long orders = orderRepository.countByCustomer(c);
        if (orders > 0) throw new RuntimeException("Cannot delete customer with existing orders");
        customerRepository.deleteById(id);
    }
}
