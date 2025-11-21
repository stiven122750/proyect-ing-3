package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService service;

    @Test
    void findAll_ShouldReturnProductList() {
        when(productRepository.findAll()).thenReturn(List.of(
                new Product("A", 10, 1),
                new Product("B", 20, 2)
        ));

        List<Product> result = service.findAll();

        assertEquals(2, result.size());
        verify(productRepository).findAll();
    }

    @Test
    void findById_ShouldReturnProduct_WhenExists() {
        Product product = new Product("Phone", 500.0, 10);
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Phone", result.get().getName());
        verify(productRepository).findById(1L);
    }

    @Test
    void create_ShouldSaveProduct_WhenValid() {
        Product product = new Product("Laptop", 1000.0, 5);

        when(productRepository.findByName("Laptop")).thenReturn(Optional.empty());
        when(productRepository.save(product)).thenReturn(product);

        Product result = service.create(product);

        assertEquals("Laptop", result.getName());
        verify(productRepository).findByName("Laptop");
        verify(productRepository).save(product);
    }

    @Test
    void create_ShouldThrowException_WhenPriceIsInvalid() {
        Product product = new Product("Laptop", 0.0, 5);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.create(product));

        assertEquals("Product price must be positive", ex.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void create_ShouldThrowException_WhenNameExists() {
        Product existing = new Product("Laptop", 900.0, 5);

        when(productRepository.findByName("Laptop")).thenReturn(Optional.of(existing));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.create(existing));

        assertEquals("Product name must be unique", ex.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void update_ShouldModifyAndSaveProduct_WhenValid() {
        Product existing = new Product("Old", 200.0, 2);
        existing.setId(1L);

        Product updated = new Product("New", 300.0, 3);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(existing)).thenReturn(existing);

        Product result = service.update(1L, updated);

        assertEquals("New", result.getName());
        assertEquals(300.0, result.getPrice());
        assertEquals(3, result.getStock());
        verify(productRepository).save(existing);
    }

    @Test
    void update_ShouldThrowException_WhenProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.update(1L, new Product("Test", 50.0, 1)));

        assertEquals("Product not found", ex.getMessage());
    }

    @Test
    void update_ShouldThrowException_WhenPriceInvalid() {
        Product existing = new Product("Phone", 500.0, 10);
        existing.setId(1L);

        Product updated = new Product("PhoneX", 0.0, 5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.update(1L, updated));

        assertEquals("Product price must be positive", ex.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void delete_ShouldDeleteProductById() {
        doNothing().when(productRepository).deleteById(1L);

        service.delete(1L);

        verify(productRepository).deleteById(1L);
    }
}
