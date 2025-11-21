package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setup() {
        // Se inicializa una categoría para ser utilizada en las pruebas
        category = new Category("Electronics");
    }

    @Test
    void shouldReturnAllCategories() {
        // Prueba que el servicio devuelva todas las categorías disponibles
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<Category> result = categoryService.findAll();

        // Verifica que la lista tenga exactamente un elemento y que se llamara al repositorio
        assertEquals(1, result.size());
        verify(categoryRepository).findAll();
    }

    @Test
    void shouldReturnCategoryById() {
        // Prueba que se obtenga una categoría correctamente mediante su ID
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Optional<Category> result = categoryService.findById(1L);

        // Se verifica que la categoría exista y sea la misma simulada
        assertTrue(result.isPresent());
        assertEquals(category, result.get());
    }

    @Test
    void shouldCreateCategorySuccessfully() {
        // Prueba la creación de una nueva categoría
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.create(category);

        // Verifica que la categoría fue guardada y retornada correctamente
        assertEquals(category, result);
        verify(categoryRepository).save(category);
    }

    @Test
    void shouldUpdateCategorySuccessfully() {
        // Prueba la actualización de una categoría existente con nuevos datos
        Category updated = new Category("New Name");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        // Se simula que el repositorio devuelve el objeto actualizado
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));

        Category result = categoryService.update(1L, updated);

        // Verificaciones sobre los nuevos valores aplicados
        assertEquals("New Name", result.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingCategory() {
        // Prueba que se lance excepción al intentar actualizar una categoría que no existe
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> categoryService.update(1L, new Category()));
    }

    @Test
    void shouldDeleteSuccessfully() {
        // Prueba que se llame al repositorio para eliminar una categoría
        categoryService.delete(1L);

        verify(categoryRepository).deleteById(1L);
    }
}
