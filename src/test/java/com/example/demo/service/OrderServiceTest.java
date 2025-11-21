package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.model.OrderEntity;
import com.example.demo.model.Product;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
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
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private ProductRepository productRepository;
    @Mock private CustomerRepository customerRepository;

    @InjectMocks private OrderService orderService;

    private Customer customer;
    private Product product;

    @BeforeEach
    void setUp() {
        // Inicializa un cliente y un producto base para las pruebas
        customer = new Customer("John Doe", "john@example.com");
        product = new Product("Laptop", 1200.0, 10);
    }

    @Test
    void shouldReturnAllOrders() {
        // Verifica que el servicio retorna la lista completa de órdenes
        when(orderRepository.findAll()).thenReturn(List.of(new OrderEntity()));

        List<OrderEntity> result = orderService.findAll();

        assertEquals(1, result.size());
        verify(orderRepository).findAll();
    }

    @Test
    void shouldFindOrderById() {
        // Verifica que se retorne correctamente una orden existente
        OrderEntity order = new OrderEntity(customer, product, 2);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderEntity result = orderService.findById(1L);

        assertEquals(order, result);
    }

    @Test
    void shouldThrowWhenOrderNotFound() {
        // Si la orden no existe, se debe lanzar una excepción
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.findById(1L));
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        // Prueba del flujo completo de creación de una orden:
        // - Cliente existe
        // - Producto existe
        // - Stock suficiente
        // - Cliente con menos de 5 órdenes previas

        long customerId = 1L;
        long productId = 2L;
        int quantity = 3;

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderRepository.countByCustomer(customer)).thenReturn(2L);
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderEntity result = orderService.create(customerId, productId, quantity);

        assertNotNull(result);
        assertEquals(quantity, result.getQuantity());
        assertEquals(7, product.getStock()); // Se reduce el stock: 10 - 3

        verify(productRepository).save(product);
        verify(orderRepository).save(result);
    }

    @Test
    void shouldThrowIfQuantityInvalid() {
        // Si la cantidad es menor o igual que cero, debe lanzar excepción
        assertThrows(RuntimeException.class, () ->
                orderService.create(1L, 2L, 0)
        );
    }

    @Test
    void shouldThrowIfStockIsNotEnough() {
        // Si el producto no tiene suficiente stock, debe lanzarse excepción
        product.setStock(2);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));

        assertThrows(RuntimeException.class, () ->
                orderService.create(1L, 2L, 3)
        );
    }

    @Test
    void shouldThrowIfCustomerHasMoreThanFiveOrders() {
        // Si el cliente ya tiene 5 o más órdenes, no se permite crear una nueva orden
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));
        when(orderRepository.countByCustomer(customer)).thenReturn(5L);

        assertThrows(RuntimeException.class, () ->
                orderService.create(1L, 2L, 1)
        );
    }

    @Test
    void shouldDeleteOrder() {
        // Verifica que el servicio invoque la eliminación del pedido por ID
        orderService.delete(1L);
        verify(orderRepository).deleteById(1L);
    }
}
