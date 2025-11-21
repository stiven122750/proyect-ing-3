package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setup() {
        customer = new Customer("John Doe", "john@example.com");
    }

    @Test
    void shouldReturnAllCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<Customer> result = customerService.findAll();

        assertEquals(1, result.size());
        verify(customerRepository).findAll();
    }

    @Test
    void shouldFindCustomerById() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(customer, result.get());
    }

    @Test
    void shouldCreateCustomerSuccessfully() {
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerService.create(customer);

        assertEquals(customer, result);
        verify(customerRepository).save(customer);
    }

    @Test
    void shouldUpdateCustomerSuccessfully() {
        Customer updated = new Customer("New Name", "new@example.com");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Customer result = customerService.update(1L, updated);

        assertEquals("New Name", result.getName());
        assertEquals("new@example.com", result.getEmail());
        verify(customerRepository).save(customer);
    }

    @Test
    void shouldThrowWhenCustomerNotFoundForUpdate() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> customerService.update(1L, new Customer()));
    }

    @Test
    void shouldDeleteCustomerSuccessfully() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(orderRepository.countByCustomer(customer)).thenReturn(0L);

        customerService.delete(1L);

        verify(customerRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingCustomerWithOrders() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(orderRepository.countByCustomer(customer)).thenReturn(3L);

        assertThrows(RuntimeException.class,
                () -> customerService.delete(1L));

        verify(customerRepository, never()).deleteById(any());
    }

    @Test
    void shouldThrowWhenDeletingNonExistingCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> customerService.delete(1L));
    }
}
