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
        // Se inicializa un cliente base para todas las pruebas
        customer = new Customer("John Doe", "john@example.com");
    }

    @Test
    void shouldReturnAllCustomers() {
        // Prueba que el servicio retorne correctamente la lista de todos los clientes
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<Customer> result = customerService.findAll();

        assertEquals(1, result.size()); // Debe haber exactamente un cliente
        verify(customerRepository).findAll(); // Verifica que se llamo al repositorio
    }

    @Test
    void shouldFindCustomerById() {
        // Verifica que se obtenga un cliente existente por su ID
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.findById(1L);

        assertTrue(result.isPresent()); // El resultado no debe estar vacío
        assertEquals(customer, result.get()); // El cliente retornado es el esperado
    }

    @Test
    void shouldCreateCustomerSuccessfully() {
        // Prueba la creación de un cliente, validando que se guarde correctamente
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerService.create(customer);

        assertEquals(customer, result);
        verify(customerRepository).save(customer);
    }

    @Test
    void shouldUpdateCustomerSuccessfully() {
        // Prueba la actualización correcta de un cliente existente
        // con nuevos valores en su información
        Customer updated = new Customer("New Name", "new@example.com");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Customer result = customerService.update(1L, updated);

        // Verifica que los valores del cliente se hayan actualizado
        assertEquals("New Name", result.getName());
        assertEquals("new@example.com", result.getEmail());

        verify(customerRepository).save(customer);
    }

    @Test
    void shouldThrowWhenCustomerNotFoundForUpdate() {
        // Si el cliente a actualizar no existe, se debe lanzar una excepción
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> customerService.update(1L, new Customer()));
    }

    @Test
    void shouldDeleteCustomerSuccessfully() {
        // Prueba que un cliente sin órdenes pueda ser eliminado
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(orderRepository.countByCustomer(customer)).thenReturn(0L);

        customerService.delete(1L);

        verify(customerRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingCustomerWithOrders() {
        // Si el cliente tiene órdenes asociadas, no debe permitirse su eliminación
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(orderRepository.countByCustomer(customer)).thenReturn(3L);

        assertThrows(RuntimeException.class,
                () -> customerService.delete(1L));

        // Se verifica que nunca se intente eliminar
        verify(customerRepository, never()).deleteById(any());
    }

    @Test
    void shouldThrowWhenDeletingNonExistingCustomer() {
        // Si el cliente no existe al intentar eliminar, se debe lanzar una excepción
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> customerService.delete(1L));
    }
}
