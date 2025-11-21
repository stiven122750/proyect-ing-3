package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.OrderEntity;
import com.example.demo.model.Customer;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByCustomer(Customer customer);
    long countByCustomer(Customer customer);
}
